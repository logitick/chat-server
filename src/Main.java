import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {


	public static void main(String[] args) {
		JQuickChatServer server = null;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

        try {
			server = new JQuickChatServer(args[0]);
			if (server.isReady()) {
                executorService.schedule(server, 0, TimeUnit.MILLISECONDS);
				System.out.println("Server created at " + server);
                Disseminator d = new Disseminator(server);
                executorService.schedule(d, 500, TimeUnit.MILLISECONDS);
				Listener l = new Listener(server);
                executorService.schedule(l, 500, TimeUnit.MILLISECONDS);
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
