import java.io.IOException;
import java.util.TimerTask;


public class Listener implements Runnable {

	private JQuickChatServer server;
	
	public Listener(JQuickChatServer server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		Client clientArr[] = server.getClients();
		for (Client c : clientArr) {
			try {
				if (c.hasMessage()) {
					String msg = c.receive();
					if (msg.equals("/quit")) {
						c.send("Disconnected");
						c.close();
						server.removeUser(c);
						JQuickChatServer.enqueueMessage(c.getUser() + " has disconnected");
						continue;
					}
					JQuickChatServer.enqueueMessage(c.getUser() + ": " + msg);
				}
			} catch (IOException e) 
			{
				
			}
		}
	}

}
