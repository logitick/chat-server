import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.TimerTask;

public class Client extends Observable{
	private String user;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private InputStreamReader inputStream;
	public Client(Socket s) throws IOException {
		socket = s;
		out = new PrintWriter(socket.getOutputStream(), true);
		inputStream = new InputStreamReader(socket.getInputStream());
		in = new BufferedReader(inputStream);
	}
	
	
	
	public boolean sentData() throws IOException {
		return in.ready();
	}
	
	public void close() throws IOException {
		out.close();
		inputStream.close();
		in.close();
		socket.close();
	}
	
	public void send(String str) {
		out.println(str+'\r');
	}
	
	public String receive() throws IOException
	{
		return in.readLine();
	}
	
	public boolean hasMessage() throws IOException {
		return in.ready();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}