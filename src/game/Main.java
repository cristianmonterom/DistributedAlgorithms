package game;

import java.net.Socket;
import java.util.ArrayList;

import common.Game;
import common.States;
import messaging.StatusResponse;

public class Main {
	static ArrayList<Client> clientList = new ArrayList<Client>();
	StatusResponse status;

	private static String hostname;
	private static int serverPort;
	private static int clientPort;
	private static boolean local;
	private static boolean debug;

	/**
	 * This is the main for video streaming project
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = new Socket();

		/**
		 * parsing arguments
		 */
		setArgumentsFromCommandLine(args);

		if (!local) {
			ClientConnection connAsClient;
			connAsClient = new ClientConnection(hostname, getServerPort());
			socket = connAsClient.establishConnection();
			ClientThread cThread = new ClientThread(socket);
			cThread.setDebug(debug);
			Thread test = new Thread(cThread);
			test.start();
		} else {
			ServerConnection connAsServer = new ServerConnection(
					getServerPort());
//			Game nGame;
//			States processState = States.StandBy;
			while (true){
				connAsServer.establishConnection();
//				ServerThread sThread = new ServerThread(connAsServer.socket);
				ServerThread sThread = ServerThread.getInstance(connAsServer.socket);
				sThread.setDebug(debug);
				Thread serverThread = new Thread(sThread);
				serverThread.start();
			}
		}
	}

	private static void setArgumentsFromCommandLine(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		parser.Parse(args);

		hostname = parser.getHostname();
		serverPort = parser.getServerPort();
		clientPort = parser.getClientPort();
		debug = parser.isDebugging();
		
		local = !hostname.equals("") ? false : true;

		System.out.print("Parameters:");
		System.out.print(" hostname=" + hostname);
		System.out.print(" serverPort=" + serverPort);
		System.out.print(" clientport=" + clientPort + "\n");
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static int getRemotePort() {
		return clientPort;
	}

}
