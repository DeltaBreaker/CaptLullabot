package tv.twitch.deltabot.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import tv.twitch.deltabot.bots.TwitchBot;
import tv.twitch.deltabot.ui.AuthGUI;
import tv.twitch.deltabot.ui.GUI;

public class Init {

	private static final String version = "1.2.3";

	public static TwitchBot twitchBot;
	public static JDA discordBot;
	public static GUI gui;
	public static boolean isRunning = true;

	public static String botName = "CaptLullabot";
	public static String twitchChannel;
	public static String twitchAuth;
	public static String discordAuth;
	public static long discordServerID;
	public static long discordServerChannelID;

	public static void main(String[] args) throws Exception {
		loadAuth();

		if (isRunning) {
			loadOps();
			loadSettings();
			startLogger();
			startConsole();
			TwitchBot.pokemonList = loadStringArray("pokemon_names.txt");

			twitchBot = new TwitchBot(botName);
			twitchBot.setVerbose(true);
			twitchBot.connect("irc.twitch.tv", 6667, twitchAuth);
			twitchBot.joinChannel(twitchChannel);

			discordBot = new JDABuilder(AccountType.BOT).setToken(discordAuth).build();

			if (args.length > 0) {
				if (!args[0].equals("nogui")) {
					gui = new GUI(twitchBot);
				}
			} else {
				gui = new GUI(twitchBot);
			}

			System.out.println("[Init]: Bot started for channel: " + twitchChannel);
		}

		while (isRunning) {
			twitchBot.tick();
			if (gui != null) {
				gui.repaint();
			}
			Thread.sleep(250L);
		}
	}

	public static void startLogger() throws Exception {
		File f = new File(System.getenv("APPDATA") + "/DeltaBot/logs");
		if (!f.exists()) {
			f.mkdirs();
		}
		File log = new File(f + "/" + LocalDate.now() + ".log");
		PrintStream out = new PrintStream(log);

		System.setOut(new ConsoleLogger(out, System.out));
		System.out.println("[Init]: Started console logger");
	}

	public static void startConsole() {
		new Thread(new ConsoleCommands()).start();
		System.out.println("[Init]: Started chat command system");
	}

	public static void loadAuth() {
		File f = new File(System.getenv("APPDATA") + "/DeltaBot/auth.keys");
		if (f.exists()) {
			Properties mySettings = new Properties();
			try {
				mySettings.load(new FileInputStream(f));
			} catch (Exception e) {
				e.printStackTrace();
			}

			twitchChannel = mySettings.getProperty("twitch_channel");
			twitchAuth = mySettings.getProperty("twitch_auth");
			discordAuth = mySettings.getProperty("discord_auth");
			discordServerID = Long.parseLong(mySettings.getProperty("discord_server_id"));
			discordServerID = Long.parseLong(mySettings.getProperty("discord_server_channel_id"));

			System.out.println("[Init]: Auth keys file loaded");
		} else {
			System.out.println("[Init]: Auth keys file not found");
			isRunning = false;
			new AuthGUI();
		}
	}

	public static void createAuth() {
		File f = new File(System.getenv("APPDATA") + "/DeltaBot");
		if (!f.exists()) {
			f.mkdirs();
		}
		try {
			PrintStream out = new PrintStream(new FileOutputStream(f + "/auth.keys"));
			out.println("/* Auth Keys */");
			out.println("/* !! Never show these to anyone !! */");
			out.println("");

			out.println("twitch_channel=" + twitchChannel);
			out.println("twitch_auth=" + twitchAuth);
			out.println("discord_auth=" + discordAuth);
			out.println("discord_server_id=" + discordServerID);
			out.println("discord_server_channel_id=" + discordServerChannelID);

			out.flush();
			out.close();

			System.out.println("[Init]: Auth keys file saved");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static void loadSettings() {
		File f = new File(System.getenv("APPDATA") + "/DeltaBot/settings.cfg");
		if (f.exists()) {
			Properties mySettings = new Properties();
			try {
				mySettings.load(new FileInputStream(f));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (mySettings.getProperty("version").equals(version)) {
				TwitchBot.twitchChannelLink = mySettings.getProperty("twitch_channel_link");
				TwitchBot.discordServerLink = mySettings.getProperty("discord_server_link");
				TwitchBot.discordNotif = Boolean.parseBoolean(mySettings.getProperty("discord_notif"));
				TwitchBot.notifTime = Integer.parseInt(mySettings.getProperty("discord_notif_time"));
				TwitchBot.timeoutTime = Integer.parseInt(mySettings.getProperty("timeout_time"));
				TwitchBot.pokeGame = Boolean.parseBoolean(mySettings.getProperty("pokemon_minigame"));
				TwitchBot.wildTime = Integer.parseInt(mySettings.getProperty("pokemon_wild_time"));
				TwitchBot.catchTime = Integer.parseInt(mySettings.getProperty("pokemon_catch_time"));
				TwitchBot.catchRate = Integer.parseInt(mySettings.getProperty("pokemon_catch_rate"));

				System.out.println("[Init]: Settings config file loaded");
			} else {
				System.out.println("[Init]: Version does not match");
				createSettings();
				loadSettings();
			}
		} else {
			System.out.println("[Init]: Settings config file not found");
			createSettings();
			loadSettings();
		}
	}

	public static void createSettings() {
		File f = new File(System.getenv("APPDATA") + "/DeltaBot");
		if (!f.exists()) {
			f.mkdirs();
		}
		try {
			PrintStream out = new PrintStream(new FileOutputStream(f + "/settings.cfg"));
			out.println("/* Bot Settings */");
			out.println("");

			out.println("version=" + version);
			out.println("twitch_channel_link=" + TwitchBot.twitchChannelLink);
			out.println("discord_server_link=" + TwitchBot.discordServerLink);
			out.println("discord_notif=" + TwitchBot.discordNotif);
			out.println("discord_notif_time=" + TwitchBot.notifTime);
			out.println("timeout_time=" + TwitchBot.timeoutTime);
			out.println("pokemon_minigame=" + TwitchBot.pokeGame);
			out.println("pokemon_wild_time=" + TwitchBot.wildTime);
			out.println("pokemon_catch_time=" + TwitchBot.catchTime);
			out.println("pokemon_catch_rate=" + TwitchBot.catchRate);

			out.flush();
			out.close();

			System.out.println("[Init]: Settings config file saved");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static void saveOps() {
		try {
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data");
			if (!f.exists()) {
				f.mkdirs();
			}
			ObjectOutputStream out = null;

			out = new ObjectOutputStream(new FileOutputStream(f + "/ops.dat"));

			out.writeInt(TwitchBot.operators.size());
			for (String s : TwitchBot.operators) {
				out.writeUTF(s);
			}

			out.close();
			System.out.println("[Init]: Operator list saved");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void loadOps() {
		try {
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data/ops.dat");
			if (f.exists()) {

				TwitchBot.operators.clear();

				ObjectInputStream in = null;
				in = new ObjectInputStream(new FileInputStream(f));

				int length = in.readInt();
				for (int i = 0; i < length; i++) {
					TwitchBot.operators.add(in.readUTF());
				}

				in.close();
				System.out.println("[Init]: Operator list loaded");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] loadStringArray(String file) {
		File f = new File(new File(".").getAbsolutePath() + "/data/" + file);
		if (f.exists()) {
			ArrayList<String> text = new ArrayList<String>();
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				for (String line; (line = br.readLine()) != null;) {
					text.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return text.toArray(new String[text.size()]);
		}

		return new String[0];
	}

	public static void savePokemon(String sender, String pokemon) {
		try {
			File dir = new File(System.getenv("APPDATA") + "/DeltaBot/data/users/" + sender);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data/users/" + sender + "/pokemon.dat");
			if (f.exists()) {
				ArrayList<String> list = new ArrayList<String>();

				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));

				int length = in.readInt();
				for (int i = 0; i < length; i++) {
					list.add(in.readUTF());
				}

				in.close();

				list.add(pokemon);

				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));

				out.writeInt(list.size());
				for (String s : list) {
					out.writeUTF(s);
				}

				out.flush();
				out.close();
			} else {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));

				out.writeInt(1);
				out.writeUTF(pokemon);

				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
