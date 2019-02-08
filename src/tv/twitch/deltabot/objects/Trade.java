package tv.twitch.deltabot.objects;

public class Trade {

	public String sender;
	public String recipient;
	public int offer;
	public int request;
	
	public int timer = 0;
	public int time = 240;
	
	public Trade(String sender, String recipient, int offer, int request) {
		this.sender = sender;
		this.recipient = recipient;
		this.offer = offer;
		this.request = request;
	}
	
	public void tick() {
		if(timer < time) {
			timer++;
		}
	}
	
}
