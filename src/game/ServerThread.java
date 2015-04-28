package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import common.BagOfItems;
import common.Game;
import common.Token;
import messaging.CheckAvailabilityRequest;
import messaging.CheckAvailabilityResponse;
import messaging.Play;
import messaging.StartGameRequest;
import messaging.StartGameResponse;
import messaging.StopStreamResponse;
import messaging.TakenServer;

public class ServerThread implements Runnable {
	private OutputStream outputStream;
	private InputStream inputStream;
	private PrintWriter out;
	private BufferedReader in;
	private boolean debug;
	private boolean busy;
	private Game newGame;
	
	public ServerThread(Socket socket) {

		boolean started = true;
		if (!started) {
			throw new RuntimeException("Not able to start native grabber!");
		}
		
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));
	}

	public ServerThread() {
	}

	@Override
	public void run() {
		try{
			receiveCheckAvailabilityRequest();
			
			sendCheckAvailabilityResponse();
			
			receiveStartGameRequest();
			
			newGame = new Game(4);
			
			sendStartGameResponse(newGame.getToken(), newGame.getBagItems());
			
		} catch (Exception e){
			printDebugLines(e.getStackTrace().toString());
		}
	}
	
	private void receiveCheckAvailabilityRequest() {
		String strFromServer = null;
		CheckAvailabilityRequest checkAvailabilityRequest;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		checkAvailabilityRequest = new CheckAvailabilityRequest();
		checkAvailabilityRequest.FromJSON(strFromServer);
		printDebugLines(checkAvailabilityRequest.ToJSON());
	}
	
	private void receiveStartGameRequest() {
		String strFromServer = null;
		StartGameRequest startGameRequest;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		startGameRequest = new StartGameRequest();
		startGameRequest.FromJSON(strFromServer);
		printDebugLines(startGameRequest.ToJSON());
	}
	
	
	private void sendCheckAvailabilityResponse() throws IOException {
		CheckAvailabilityResponse request = new CheckAvailabilityResponse(this.busy);
		this.busy = true;
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}
	
	private void sendStartGameResponse(Token token, BagOfItems bag) throws IOException {
		String message = null;
//		if (this.busy){
//			TakenServer request = new TakenServer();
//			message = request.ToJSON();
//		} else {
			StartGameResponse request = new StartGameResponse(bag.ToJSON(), token.getTokenSession());
//			request.setBag(bag.ToJSON());
//			request.setToken(token.getTokenSession());
			message = request.ToJSON();
			this.busy = true;
//		}
		out.println(message);
		printDebugLines(message);
	}
	
	public void setDebug(boolean var){
		this.debug = var;
	}
	
	private void printDebugLines(String message){
		if (this.debug){
			System.out.println("Server: " + message);
		}		
	}

}
