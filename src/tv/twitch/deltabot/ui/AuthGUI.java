package tv.twitch.deltabot.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import tv.twitch.deltabot.core.Init;

public class AuthGUI extends JFrame {

	private static final long serialVersionUID = -6914595748816604055L;

	public JTextArea twitchChannel, twitchAuth, discordAuth, discordServerID, discordServerChannelID;
	public JButton confirm;
	
	public Border border = BorderFactory.createLineBorder(Color.BLACK);
	
	public AuthGUI() {
		setTitle("Auth Keys");
		setSize(640, 240);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		twitchChannel = new JTextArea("Twitch Channel");
		twitchChannel.setBounds(10, 11, 614, 18);
		twitchChannel.setBorder(border);
		
		twitchAuth = new JTextArea("Twitch Auth Token");
		twitchAuth.setBounds(10, 41, 614, 18);
		twitchAuth.setBorder(border);
		
		discordAuth = new JTextArea("Discord Auth Token");
		discordAuth.setBounds(10, 71, 614, 18);
		discordAuth.setBorder(border);
		
		discordServerID = new JTextArea("Discord Server ID");
		discordServerID.setBounds(10, 101, 614, 18);
		discordServerID.setBorder(border);
		
		discordServerChannelID = new JTextArea("Discord Server Channel ID");
		discordServerChannelID.setBounds(10, 131, 614, 18);
		discordServerChannelID.setBorder(border);
		
		confirm = new JButton("Confirm");
		confirm.setBounds(10, 161, 614, 18);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Init.twitchChannel = twitchChannel.getText();
				Init.twitchAuth = twitchAuth.getText();
				Init.discordAuth = discordAuth.getText();
				Init.discordServerID = Long.parseLong(discordServerID.getText());
				Init.discordServerChannelID = Long.parseLong(discordServerChannelID.getText());
				Init.createAuth();
				System.exit(0);
			}
		});
		
		add(twitchChannel);
		add(twitchAuth);
		add(discordAuth);
		add(discordServerID);
		add(discordServerChannelID);
		add(confirm);
		
		setVisible(true);
	}
	
}
