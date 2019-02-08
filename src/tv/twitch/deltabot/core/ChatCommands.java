package tv.twitch.deltabot.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import tv.twitch.deltabot.bots.TwitchBot;

public enum ChatCommands {

	_discord {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			bot.sendMessage(channel, "Join our Twitch server on Discord: https://discord.gg/PHrkGzp");
			System.out.println("[Chat]: Command 'Discord' ran successfully");
		}
	},

	_discordnotif {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				TwitchBot.discordNotif = Boolean.parseBoolean(message.split(" ")[1]);
				bot.sendMessage(channel, "Set Discord notif to " + TwitchBot.discordNotif + ".");
				System.out.println("[Chat]: Set Discord notif to " + TwitchBot.discordNotif + ".");
				Init.createSettings();
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_discordnotiftime {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				TwitchBot.notifTime = Integer.parseInt(message.split(" ")[1]) * 60 * 1000 / 250;
				bot.sendMessage(channel,
						"Set Discord notif timer to: " + Integer.parseInt(message.split(" ")[1]) + "m");
				System.out.println(
						"[Chat]: Set Discord notif timer to: " + Integer.parseInt(message.split(" ")[1]) + "m");
				Init.createSettings();
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_credits {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			bot.sendMessage(channel, "DeltaBreaker created me! :)");
		}
	},

	_uptime {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			int seconds = TwitchBot.uptime / 4;
			int minutes = Math.floorDiv(seconds, 60);
			seconds -= minutes * 60;
			int hours = Math.floorDiv(minutes, 60);
			minutes -= hours * 60;
			bot.sendMessage(channel, "We've been live for " + hours + "hrs " + minutes + "m " + seconds + "s");
		}
	},

	_timeouttime {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				TwitchBot.timeoutTime = Integer.parseInt(message.split(" ")[1]);
				bot.sendMessage(channel, "Set timeout time to: " + TwitchBot.timeoutTime + "s");
				System.out.println("[Chat]: Set timeout time to: " + TwitchBot.timeoutTime + "s");
				Init.createSettings();
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_newpoll {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				bot.voting = false;
				bot.voteTimer = 0;
				bot.voteOptions.clear();
				bot.voters.clear();
				bot.votes = new int[0];
				bot.sendMessage(channel, "New poll created.");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_addvote {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				if (!bot.voting) {
					bot.voteOptions.add(message.substring(9));
					bot.sendMessage(channel, "Added: " + message.substring(9) + " to the poll.");
				} else {
					bot.sendMessage(channel, "The poll has already started!");
				}
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_startpoll {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				bot.voteTime = Integer.parseInt(args[1]) * 4;
				bot.voteTimer = 0;
				bot.votes = new int[bot.voteOptions.size()];
				for (int i = 0; i < bot.votes.length; i++) {
					bot.votes[i] = 0;
				}
				bot.voting = true;
				bot.voters.clear();
				bot.sendMessage(channel, "Starting the poll with " + args[1] + "s!");
				String votes = "The choices are: ";
				for (int i = 0; i < bot.voteOptions.size(); i++) {
					votes += "[" + (i + 1) + "]: " + bot.voteOptions.get(i) + " | ";
				}
				bot.sendMessage(channel, votes);
				bot.sendMessage(channel, "Use !vote 'number' to cast your vote!");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_vote {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (bot.voting) {
				boolean voted = false;
				for (String s : bot.voters) {
					if (s.equals(sender)) {
						voted = true;
						break;
					}
				}
				if (!voted) {
					String[] args = message.split(" ");
					bot.votes[Integer.parseInt(args[1]) - 1]++;
					bot.voters.add(sender);
				} else {
					bot.sendMessage(channel, "You've already voted!");
				}
			} else {
				bot.sendMessage(channel, "There is no poll currently running.");
			}
		}
	},

	_rock {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			String[] opts = { "rock", "paper", "scissors" };
			bot.sendMessage(channel, "I pick " + opts[new Random().nextInt(opts.length)] + "!");
		}
	},

	_paper {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			String[] opts = { "rock", "paper", "scissors" };
			bot.sendMessage(channel, "I pick " + opts[new Random().nextInt(opts.length)] + "!");
		}
	},

	_scissors {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			String[] opts = { "rock", "paper", "scissors" };
			bot.sendMessage(channel, "I pick " + opts[new Random().nextInt(opts.length)] + "!");
		}
	},

	_checkin {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (bot.points.containsKey(sender)) {
				bot.sendMessage(channel, "You're already checked in!");
			} else {
				bot.loadUser(sender);
				bot.sendMessage(channel, "Welcome to the stream, " + sender + "! :)");
			}
		}
	},

	_balance {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (bot.points.containsKey(sender)) {
				bot.sendMessage(channel, "Your balance is " + bot.points.get(sender) + ".");
			} else {
				bot.sendMessage(channel, "You havn't checked in yet!");
			}
		}
	},

	_balanceadd {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				if (bot.points.containsKey(args[2])) {
					bot.points.put(args[2], bot.points.get(args[2]) + Integer.parseInt(args[1]));
					bot.sendMessage(channel, "Added " + args[1] + " to " + args[2] + "'s balance.");
				} else {
					bot.sendMessage(channel, "User has not check in yet!");
				}
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_balancesub {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				if (bot.points.containsKey(args[2])) {
					bot.points.put(args[2], Math.max(bot.points.get(args[2]) - Integer.parseInt(args[1]), 0));
					bot.sendMessage(channel, "Removed " + args[1] + " from " + args[2] + "'s balance.");
				} else {
					bot.sendMessage(channel, "User has not check in yet!");
				}
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_notiflive {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				Init.discordBot.getGuildById(Init.discordServerID).getTextChannelById(536043563234885668L)
						.sendMessage("@everyone We just went live! https://www.twitch.tv/captlullaby").queue();
				bot.sendMessage(channel, "Notification sent!");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_encounters {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				TwitchBot.pokeGame = Boolean.parseBoolean(args[1]);
				bot.sendMessage(channel, "Set wild encounters to " + TwitchBot.pokeGame + ".");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_encountertime {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				TwitchBot.wildTime = Integer.parseInt(args[1]) * 60 * 4;
				bot.sendMessage(channel, "Set encounter rate to " + args[1] + "m.");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_catchtime {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				TwitchBot.wildTime = Integer.parseInt(args[1]) * 4;
				bot.sendMessage(channel, "Set catch time to " + args[1] + "s.");
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},

	_throw {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (TwitchBot.pokeGame) {
				if (bot.wildAppeared) {
					boolean thrown = false;
					for (String s : bot.trainers) {
						if (s.equals(sender)) {
							thrown = true;
						}
					}
					if (!thrown) {
						bot.trainers.add(sender);
						if (new Random().nextInt(100) < 50) {
							bot.sendMessage(channel,
									sender + " has caught the " + TwitchBot.pokemonList[bot.currentPokemon] + "!");
							bot.wildAppeared = false;
							bot.caught = true;
							Init.savePokemon(sender, TwitchBot.pokemonList[bot.currentPokemon]);
						} else {
							bot.sendMessage(channel, "It broke free!");
						}
					} else {
						bot.sendMessage(channel, "You dont have any pokeballs!");
					}
				} else {
					bot.sendMessage(channel, "Threre's nothing there...");
				}
			} else {
				bot.sendMessage(channel, "That feature is currently not enabled!");
			}
		}
	},

	_pc {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			File f = new File(System.getenv("APPDATA") + "/DeltaBot/data/users/" + sender + "/pokemon.dat");
			if (f.exists()) {
				String[] args = message.split(" ");
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
				if (args.length == 1) {
					bot.sendMessage(channel, "You have " + in.readInt() + " Pokemon in the PC!");
				} else {
					ArrayList<String> pokemon = new ArrayList<String>();
					int length = in.readInt();
					for (int i = 0; i < length; i++) {
						pokemon.add(in.readUTF());
					}
					if (Integer.parseInt(args[1]) - 1 >= 0 && Integer.parseInt(args[1]) - 1 < pokemon.size()) {
						bot.sendMessage(channel, "You have a " + pokemon.get(Integer.parseInt(args[1]) - 1)
								+ " in slot " + (Integer.parseInt(args[1])) + ".");
					} else {
						bot.sendMessage(channel, "There's nothing there!");
					}
				}
				in.close();
			} else {
				bot.sendMessage(channel, "You don't have any Pokemon!");
			}
		}
	},

	_encounter {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				if (!bot.wildAppeared) {
					bot.wildTimer = TwitchBot.wildTime;
				}
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},
	
	_op {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				TwitchBot.operators.add(args[1].toLowerCase());
				bot.sendMessage(channel, "Added " + args[1] + " to operator list.");
				Init.saveOps();
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	},
	
	_deop {
		@Override
		public void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
				String message, boolean operator) throws Exception {
			if (operator) {
				String[] args = message.split(" ");
				for (int i = 0; i < TwitchBot.operators.size(); i++) {
					if (args[1].equals(TwitchBot.operators.get(i))) {
						TwitchBot.operators.remove(i);
						i--;
						bot.sendMessage(channel, "Removed " + args[1] + " from operator list.");
						Init.saveOps();
					}
				}
			} else {
				bot.sendMessage(channel, "You do not have the correct permissions to do that!");
			}
		}
	};

	public abstract void getCommand(TwitchBot bot, String channel, String sender, String login, String hostname,
			String message, boolean operator) throws Exception;

}