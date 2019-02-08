package tv.twitch.deltabot.objects;

public class Message {

	public String sender;
	public String message;
	public long time;
	
	public Message(String sender, String message, long time) {
		this.sender = sender;
		this.message = message;
		this.time = time;
	}
	
}
