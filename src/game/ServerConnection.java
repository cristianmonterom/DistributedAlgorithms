package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import common.Constants;

public class ServerConnection extends Connection {
	private int port = 0;

	public ServerConnection() {
		try {
			server = new ServerSocket(Constants.PORT.getValue());
			socket = new Socket();
		} catch (IOException connEx) {
			connEx.printStackTrace();
		}
	}

	public ServerConnection(int _port) {
		try {
			this.port = _port;
			server = new ServerSocket(this.port);
			socket = new Socket();
		} catch (IOException connEx) {
			connEx.printStackTrace();
		}
	}

	@Override
	public Socket establishConnection() {
		try {
//			System.out.println("port " + String.valueOf(this.port));
//			System.out.println("waiting for connection request");
			socket = server.accept();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return socket;
	}
}