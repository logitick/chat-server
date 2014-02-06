
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.Vector;


public class JQuickChatServer implements Runnable {
	
	private static Vector<String> messageQueue = new Vector<String>();
	private ServerSocket server;
	private Vector<Client> clients = new Vector<Client>();
	private final static int PORT = 5001;
	private Timer timer = new Timer();

	public JQuickChatServer(String ip) throws UnknownHostException, IOException {
		this(JQuickChatServer.stringToAddr(ip));
	}
	
	public JQuickChatServer(byte[] addr) throws UnknownHostException, IOException {
		this(InetAddress.getByAddress(addr));
	}
	
	public JQuickChatServer(InetAddress hostAddress) throws IOException {
		this.server = new ServerSocket( PORT, 10,  hostAddress);
	}
	
	protected static byte[] stringToAddr(String ip) {

	    byte addr[] = new byte[4];
	    int beg = 0;
	    int end = ip.indexOf('.'); // index of the . character in the ip address
	    for (int i = 0; i < addr.length; i++) {
	        addr[i] = Byte.parseByte(ip.substring(beg, end));
	        if (ip.indexOf('.') > -1)
	            ip = ip.substring(end+1, ip.length());
	        end = ip.indexOf('.') > -1 ? ip.indexOf('.') : ip.length();
	    }
	    return addr;
	}
	
	
	private void closeClients() throws IOException {
		Client client = null;
		while (!clients.isEmpty()) {
			client = clients.remove(0);
			client.close();
		}
	}
	
	public void close() throws IOException {
		this.closeClients();
		server.close();
	}
	
	public Client[] getClients() {
		Client clientArr[] = new Client[clients.size()];
		return clients.toArray(clientArr);
	}
	
	public void removeUser(Client c) {
		clients.remove(c);
	}
	

	@Override
	public void run() {
		
		while (!isClosed()) {
			try {
				Client c = new Client(server.accept());
				c.setUser(c.receive());
				clients.add(c);
				messageQueue.add("new client connected.");
			} catch (IOException e) {
			}
			
		}
	}
	
	public static String[] getMessageQueue() {
		String msg[] = new String[messageQueue.size()];
		msg = messageQueue.toArray(msg);
		return msg;
	}
	
	public static void clearMessageQueue() {
		messageQueue.removeAllElements();
	}
	
	public static void enqueueMessage(String msg) {
        synchronized (messageQueue) {
            messageQueue.add(msg);
        }

	}
	
	public boolean isClosed() {
		return server.isClosed();
	}
	
	public boolean isReady() {
		return server.isBound();
	}
	
	public String toString() {
		return server.getInetAddress().toString() + ":" + PORT ;
	}
}
