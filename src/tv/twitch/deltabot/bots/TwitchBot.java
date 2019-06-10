package tv.twitch.deltabot.bots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import org.jibble.pircbot.PircBot;

import tv.twitch.deltabot.core.ChatCommands;
import tv.twitch.deltabot.core.Init;
import tv.twitch.deltabot.objects.Message;
import tv.twitch.deltabot.objects.Trade;

public class TwitchBot extends PircBot {

	public static int catchRate = 30;
	public static int logLimit = 100;
	public static int notifTime = 7200;
	public static int wildTime = 10800;
	public static int catchTime = 240;
	public static boolean discordNotif = true;
	public static boolean pokeGame = true;
	public static ArrayList<String> operators = new ArrayList<String>();
	public static int uptime = 0;
	public static int timeoutTime = 180;
	public static String[] pokemonList;

	public ArrayList<Message> chatLog = new ArrayList<Message>();
	public ArrayList<String> staticChatLog = new ArrayList<String>();
	public ArrayList<String> voteOptions = new ArrayList<String>();
	public ArrayList<String> voters = new ArrayList<String>();
	public ArrayList<String> trainers = new ArrayList<String>();
	public ArrayList<Trade> trades = new ArrayList<Trade>();
	public TreeMap<String, Integer> points = new TreeMap<String, Integer>();
	public int[] votes = new int[0];

	public int notifTimer = 0;
	public int wildTimer = 0;
	public int currentPokemon = 0;
	public boolean wildAppeared = false;
	public int catchTimer = 0;
	public boolean caught = false;
	public boolean voting = false;
	public int voteTimer = 0;
	public int voteTime = 0;

	public static String discordServerLink = "https://discord.gg/PHrkGzp";
	public static String twitchChannelLink = "https://www.twitch.tv/captlullaby";
	public static String specLink = "https://pcpartpicker.com/list/wmJGCb";
	public static String commandLink = "https://raw.githubusercontent.com/DeltaBreaker/CaptLullabot/master/Commands.txt";
	
	public TwitchBot(String botName) {
		this.setName(botName);
	}
	
	public void tick() {
		uptime++;
		for (String s : points.keySet()) {
			points.put(s, points.get(s) + 1);
		}
		for (int i = 0; i < trades.size(); i++) {
			trades.get(i).tick();
			if (trades.get(i).timer == trades.get(i).time) {
				trades.remove(i);
				i--;
				sendMessage(Init.twitchChannel, "The trade expired.");
			}
		}
		if (voting) {
			if (voteTimer < voteTime) {
				voteTimer++;
				if (voteTimer == voteTime - 40) {
					sendMessage(Init.twitchChannel, "10 seconds remaining!");
				}
			} else {
				int winner = 0;
				for (int i = 0; i < votes.length; i++) {
					if (votes[i] > votes[winner]) {
						winner = i;
					}
				}
				sendMessage(Init.twitchChannel, "The poll winner is: " + voteOptions.get(winner));
				voting = false;
			}
		}
		if (pokeGame) {
			if (!wildAppeared) {
				if (wildTimer < wildTime) {
					wildTimer++;
				} else {
					wildAppeared = true;
					wildTimer = 0;
					caught = false;
					currentPokemon = new Random().nextInt(pokemonList.length);
					trainers.clear();
					catchTimer = 0;
					sendMessage(Init.twitchChannel, "Encounter: A wild " + pokemonList[currentPokemon] + " appeared!");
					sendMessage(Init.twitchChannel, "You have one pokeball. Use !throw to give it a shot!");
				}
			} else {
				if (catchTimer < catchTime) {
					catchTimer++;
					if (catchTimer == catchTime - 40) {
						sendMessage(Init.twitchChannel, "10 seconds remaining!");
					}
				} else {
					catchTimer = 0;
					if (!caught) {
						sendMessage(Init.twitchChannel, "It ran away...");
					}
					wildAppeared = false;
				}
			}
		}
		if (discordNotif) {
			if (notifTimer < notifTime) {
				notifTimer++;
			} else {
				sendMessage(Init.twitchChannel,
						"Want a place to hang out? Join our Twitch server on Discord: " + discordServerLink);
				notifTimer = 0;
			}
		}
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		chatLog.add(new Message(sender, message, System.currentTimeMillis()));
		staticChatLog.add(sender + ": " + message);
		if (chatLog.size() > logLimit) {
			chatLog.remove(0);
		}
		for (int i = 0; i < chatLog.size(); i++) {
			if (System.currentTimeMillis() - chatLog.get(i).time > 60000) {
				chatLog.remove(i);
				i--;
			}
		}

		// Check for spam
		int matches = 0;
		int previous = 0;
		for (int i = 0; i < chatLog.size(); i++) {
			if (sender.equals(chatLog.get(i).sender)) {
				previous++;
				int charMatch = 0;
				for (int c = 0; c < message.length(); c++) {
					if (c < chatLog.get(i).message.length()) {
						if (message.charAt(c) == chatLog.get(i).message.charAt(c)) {
							charMatch++;
						}
					}
				}
				if ((double) charMatch / (double) message.length() >= 0.8) {
					matches++;
				}
			}
			if (matches >= 5 || previous >= 30) {
				boolean operator = false;
				for (String s : operators) {
					if (s.equals(sender)) {
						operator = true;
						break;
					}
				}
				if (!operator) {
					sendMessage(channel, "/timeout " + sender + " " + timeoutTime);
					sendMessage(channel, sender + ", please don't spam! :)");
				}
				for (int d = 0; d < chatLog.size(); d++) {
					if (sender.equals(chatLog.get(d).sender)) {
						chatLog.remove(d);
						d--;
					}
				}
				
				break;
			}
		}

		// Process commands
		String[] args = message.toLowerCase().replace("!", "_").split(" ");
		if (args.length > 0) {
			for (ChatCommands key : ChatCommands.values()) {
				if (key.name().equals(args[0])) {
					try {
						boolean operator = false;
						for (String s : operators) {
							if (s.equals(sender)) {
								operator = true;
								break;
							}
						}
						ChatCommands.valueOf(args[0]).getCommand(this, channel, sender, login, hostname, message,
								operator);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("[Chat]: Error using command");
					}
					break;
				}
			}
		}
	}

	public void loadUser(String sender) {
		try {
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data/users/" + sender + "/points.dat");
			if (f.exists()) {
				if (!points.containsKey(sender)) {
					ObjectInputStream in = null;
					in = new ObjectInputStream(new FileInputStream(f));

					points.put(in.readUTF(), in.readInt());

					in.close();
					System.out.println("[Init]: User: " + sender + " loaded");
				} else {
					System.out.println("[Init]: User already loaded");
				}
			} else {
				createUser(sender);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveUser(String sender) {
		try {
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data/users/" + sender);
			if (!f.exists()) {
				f.mkdirs();
			}
			if (points.containsKey(sender)) {
				ObjectOutputStream out = null;

				out = new ObjectOutputStream(new FileOutputStream(f + "/points.dat"));

				out.writeUTF(sender);
				out.writeInt(points.get(sender));

				out.close();
				System.out.println("[Init]: User: " + sender + " saved");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void createUser(String sender) {
		if (!points.containsKey(sender)) {
			points.put(sender, 0);
			saveUser(sender);
			System.out.println("[Init]: User: " + sender + " created");
		}
	}

}
