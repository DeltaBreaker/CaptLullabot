package tv.twitch.deltabot.core;

import java.util.Scanner;

import tv.twitch.deltabot.bots.TwitchBot;

public class ConsoleCommands implements Runnable {

	private Scanner input;

	public void run() {
		input = new Scanner(System.in);
		System.out.println("[Console]: Console initialized");
		while (Init.isRunning) {
			String[] command = input.nextLine().split(" ");
			for (Commands k : Commands.values()) {
				if (k.name().equals(command[0])) {
					Commands.valueOf(command[0]).run(command);
				}
			}
		}
	}

}

enum Commands {

	stop {
		@Override
		public void run(String[] command) {
			try {
				for (String s : Init.twitchBot.points.keySet()) {
					Init.twitchBot.saveUser(s);
				}
				Init.createSettings();
				Init.saveOps();
				Init.twitchBot.disconnect();
				Init.twitchBot.dispose();
				Init.discordBot.shutdownNow();
				Init.isRunning = false;
				System.out.println("[Console]: Shutting down " + Init.botName);
			} catch (Exception e) {
				System.exit(0);
			}
		}
	},

	op {
		@Override
		public void run(String[] command) {
			TwitchBot.operators.add(command[1].toLowerCase());
			System.out.println("[Console]: Promoted: " + command[1]);
			Init.saveOps();
		}
	},

	deop {
		@Override
		public void run(String[] command) {
			for (int i = 0; i < TwitchBot.operators.size(); i++) {
				if (command[1].equals(TwitchBot.operators.get(i))) {
					TwitchBot.operators.remove(i);
					i--;
					System.out.println("[Console]: Demoted: " + command[1]);
					Init.saveOps();
				}
			}
		}
	};

	public abstract void run(String[] command);

}