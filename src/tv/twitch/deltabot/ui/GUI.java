package tv.twitch.deltabot.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import tv.twitch.deltabot.bots.TwitchBot;
import tv.twitch.deltabot.core.Init;

public class GUI extends JFrame {

	private static final long serialVersionUID = -4895524989184026625L;

	public TwitchBot bot;
	public JButton op, deop, shutdown;
	public JTextArea user;
	
	public Border border = BorderFactory.createLineBorder(Color.BLACK);
	
	public GUI(TwitchBot bot) {
		this.bot = bot;

		setSize(335, 120);
		setResizable(false);
		setLayout(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);

		op = new JButton("Op");
		op.setBounds(10, 10, 70, 20);
		op.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TwitchBot.operators.add(user.getText().toLowerCase());
				System.out.println("[Console]: Promoted: " + user.getText());
				Init.saveOps();
			}
		});
		
		deop = new JButton("De-op");
		deop.setBounds(90, 10, 70, 20);
		deop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < TwitchBot.operators.size(); i++) {
					if (user.getText().toLowerCase().equals(TwitchBot.operators.get(i))) {
						TwitchBot.operators.remove(i);
						i--;
						System.out.println("[Console]: Demoted: " + user.getText());
					}
				}
				Init.saveOps();
			}
		});
		
		shutdown = new JButton("Shutdown");
		shutdown.setBounds(170, 10, 150, 20);
		shutdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
					dispose();
					System.out.println("[Console]: Shutting down " + Init.botName);
					System.exit(0);
				} catch (Exception e) {
					System.exit(0);
				}
			}
		});
		
		user = new JTextArea("User");
		user.setBounds(10, 40, 150, 18);
		user.setBorder(border);
		
		add(op);
		add(deop);
		add(shutdown);
		add(user);
		
		setVisible(true);
	}

}
