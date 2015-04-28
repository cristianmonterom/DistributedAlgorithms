package game;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author cristian
 *
 */

public class ClientConnection extends Connection {

	String hostname = "";
	int port = 0;
	public ClientConnection(String host) {
		this.hostname = host;
	}

	public ClientConnection(String host, int _port) {
		this.hostname = host;
		this.port = _port;
	}

	@Override
	public Socket establishConnection() {
		try {
			socket = new Socket(this.hostname, this.port);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return socket;
	}
}