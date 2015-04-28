package game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private PrintWriter out;
	private BufferedReader in;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int servicePort;
	private String ipAddress;
	private Socket socketRef;

	public Client(){}

	public Client(Socket socket) {
		socketRef = socket;
		try{
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));

		
		ipAddress = socket.getInetAddress().toString().replace("/", "");
	}

	public PrintWriter getOut() {
		return out;
	}

	public BufferedReader getIn() {
		return in;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Socket getSocketRef() {
		return socketRef;
	}
}