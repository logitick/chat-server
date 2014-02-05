import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;


public class Main {


	public static void main(String[] args) {
		JQuickChatServer server = null;
		Timer timer = new Timer(true);
		
		try {
			server = new JQuickChatServer(args[0]);
			if (server.isReady()) {
				System.out.println("Server created at " + server);
				Disseminator d = new Disseminator(server);
				timer.schedule(d, 0, 500);
				Listener l = new Listener(server);
				timer.schedule(l,  0, 500);
				server.run();
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("The IPv4 address must be specified in the command line.");
			System.out.println("Ex: java Main 192.168.0.1");
			System.exit(1);
		} catch (UnknownHostException e) {
			System.out.println("Cannot bind the host. Are you sure about the IP address?");
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} finally {
			try {
				server.close();
				System.out.println("Server Closed");
			} catch (Exception e) {}
		}
	}

}
