package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import common.BagOfItems;

import messaging.RequestResponse;
import messaging.StatusResponse;
import messaging.RequestResponseFactory;

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

//		System.out.println(new BagOfItems(3).ToJSON());
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
			while (true){
				connAsServer.establishConnection();
				ServerThread sThread = new ServerThread(connAsServer.socket);
				sThread.setDebug(debug);
				Thread serverThread = new Thread(sThread);
				serverThread.start();
			}
		}

		/**
		 * in the next loop, the system acting as a server side must delete the
		 * clients once it was disconnected
		 */
		// boolean handover = true;
		// StatusResponse statusMsgResp = null;

		// while (true) {
		// socket = connAsServer.establishConnection();

		// if (clientList.size() < Constants.MAX_CLIENTS.getValue()) {
		// Client aNewClient = new Client(socket);
		// clientList.add(aNewClient);
		//
		// statusMsgResp = new
		// StatusResponse(local,clientList.size()-1, ratelimit, handover);
		//
		// Thread client = new
		// Thread(new ClientThread(aNewClient, statusMsgResp, clientList));
		//
		// client.start();
		// } else {
		// OutputStream outputStream = null;
		// PrintWriter out;
		//
		// OverloadResponse overLoadResp = null;
		//
		// String ipOfServer = null;
		// if(!hostname.equals("")){
		// ipOfServer = hostname;
		// }
		//
		// // overLoadResp =
		// // new OverloadResponse(clientList, ipOfServer, remotePort );
		//
		// try{
		// outputStream = socket.getOutputStream();
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// }
		// out = new PrintWriter(outputStream, true);
		// out.println(overLoadResp.ToJSON());
		// }
		// }
	}

	private static Socket notOverloadedReceived(Socket connectionSocket) {
		Socket retSocket = null;
		retSocket = receiveResponse(connectionSocket);
		return retSocket;
	}

	private static Socket receiveResponse(Socket initialSocket) {
		String strFromServer = null;
		RequestResponse rcvdRespFromServer;
		RequestResponseFactory helperOverloadMsg = new RequestResponseFactory();
		InputStream inputStream = null;
		BufferedReader in;

		do {
			try {
				inputStream = initialSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			in = new BufferedReader(new InputStreamReader(inputStream));

			try {
				strFromServer = in.readLine();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}

			System.err.println(strFromServer);
			// rcvdRespFromServer = "asdf";
			//
			rcvdRespFromServer = helperOverloadMsg.FromJSON(strFromServer);
			// if(rcvdRespFromServer!=null)
			// {
			// newIpAddr =
			// ((OverloadResponse)
			// rcvdRespFromServer).getAllClients().get(0).getIpAddress();
			// newPort =
			// ((OverloadResponse)
			// rcvdRespFromServer).getAllClients().get(0).getServicePort();
			//
			// System.err.println("reconnecting to: "+newIpAddr+" port:"+newPort);
			// connAsClient = new ClientConnection(newIpAddr,newPort);
			// initialSocket = connAsClient.establishConnection();
			// }
		} while (rcvdRespFromServer != null);

		return initialSocket;
	}

	private static void setArgumentsFromCommandLine(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		parser.Parse(args);

		hostname = parser.getHostname();
		serverPort = parser.getServerPort();
		clientPort = parser.getClientPort();
		debug = parser.isDebugging();
		
		local = !hostname.equals("") ? false : true;

		System.out.println("Parameters:");
		System.out.print("hostname=" + hostname);
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
