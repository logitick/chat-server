import java.util.TimerTask;


public class Disseminator implements Runnable{
	private JQuickChatServer server;
	public Disseminator(JQuickChatServer server) {
		this.server = server;
	}
	@Override
	public void run() {
        while (server.isReady()) {
            Client clients[] = server.getClients();
            String messages[] = JQuickChatServer.getMessageQueue();
            for (String msg : messages)
                for (Client c : clients)
                    c.send(msg);

            JQuickChatServer.clearMessageQueue();
        }

	
	}

}
