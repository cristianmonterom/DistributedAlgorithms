package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import messaging.CheckAvailabilityRequest;
import messaging.CheckAvailabilityResponse;
import messaging.PlayRequest;
import messaging.PlayResponse;
import messaging.RequestResponseFactory;
import messaging.StartGameRequest;
import messaging.StartGameResponse;
import messaging.StopGameRequest;
import messaging.StopGameResponse;
import common.BagOfItems;
import common.Game;
import common.Item;
import common.ItemType;
import common.States;
import common.Token;

public class ServerThread implements Runnable {
	private OutputStream outputStream;
	private InputStream inputStream;
	private PrintWriter out;
	private BufferedReader in;
	private boolean debug;
	private boolean busy;
	private Game newGame;
	private States processState;
	private int counterPlays = 0;

	public ServerThread(Socket socket) {
		processState = States.StandBy;
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
		String strFromServer = null;
		while (this.processState != States.GameOver) {
			try {
				do {
					strFromServer = in.readLine();
				} while (strFromServer.isEmpty());
			} catch (IOException e1) {
				System.out.println("out");
				String error = e1.getMessage();
			}

			try {
				printDebugLines("state: " + this.processState);
				switch (this.processState) {
				case StandBy:
					switch (RequestResponseFactory.getStateMessage(
							this.processState, strFromServer)) {
					case CheckAvailability:
						receiveCheckAvailabilityRequest(strFromServer);
						sendCheckAvailabilityResponse();
						break;
					case StartGame:
						int players = receiveStartGameRequest(strFromServer);
						newGame = new Game(players);
						sendStartGameResponse(newGame.getToken(),
								newGame.getBagItems());
						this.processState = States.GameStarted;
						break;
					default:
						printDebugLines("Invalid message type in state: " + this.processState);
						break;
					}
					break;
				case GameStarted:
					switch (RequestResponseFactory.getStateMessage(
							this.processState, strFromServer)) {
					case Play:
						if (this.counterPlays < this.newGame.getNumPlayers()){
							PlayRequest pr = receivePlayRequest(strFromServer);
							String message = this.newGame.validTurn(pr.getToken()) ? "" : "Incorrect Token";
							JSONArray items = pr.getItems();
							JSONParser parser = new JSONParser();
							this.newGame.addPlayer(pr.getPlayer());
							for (int i = 0; i< items.size(); i++){
								JSONObject obj = (JSONObject) parser.parse(items.get(i).toString());
								ItemType newItemType = ItemType.getByName(obj.get("item").toString());
								Item newItem = new Item(newItemType, Integer.parseInt(obj.get("amount").toString()));
								this.newGame.takenItem(newItemType);
								this.newGame.addPlayerItem(pr.getPlayer(), newItem);
							}
							sendPlayResponse(this.newGame.getBagItems().getItemJsonArray(), message);
						} 
						
						if (this.counterPlays == this.newGame.getNumPlayers()) {
							sendResults();
							this.processState = States.GameOver;
						}
						break;

					default:
						break;
					}
					break;
				case GameOver:
					switch (RequestResponseFactory.getStateMessage(
							this.processState, strFromServer)) {
					case StopGame:
						 receiveStopGameRequest(strFromServer);
						 sendStopGameResponse();
						 this.processState = States.StandBy;
						break;
					default:
						break;
					}
//			 default:
//				
//				 break;
				}

			} catch (Exception e) {
				String error = e.getMessage();
				// printDebugLines(e.getStackTrace().toString());
			}
		}
	}

	private void sendResults() {
		
	}

	private void sendPlayResponse(JSONArray bag, String message) {
		PlayResponse response = new PlayResponse(bag, message);
		out.println(response.ToJSON());
		printDebugLines(response.ToJSON());
	}

	private boolean receiveCheckAvailabilityRequest(String strFromServer) {
		try {
			CheckAvailabilityRequest checkAvailabilityRequest;
			checkAvailabilityRequest = new CheckAvailabilityRequest();
			checkAvailabilityRequest.FromJSON(strFromServer);
			printDebugLines(checkAvailabilityRequest.ToJSON());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int receiveStartGameRequest(String strFromServer) {
		StartGameRequest startGameRequest = new StartGameRequest();
		startGameRequest.FromJSON(strFromServer);
		printDebugLines(startGameRequest.ToJSON());
		return startGameRequest.getNumPlayers();
	}

	private void receiveStopGameRequest(String strFromServer) {
		StopGameRequest stopGameRequest = new StopGameRequest();
		stopGameRequest.FromJSON(strFromServer);
		printDebugLines(stopGameRequest.ToJSON());
	}

	private PlayRequest receivePlayRequest(String strFromServer) {
		try {
			PlayRequest playRequest = new PlayRequest(this.newGame.getToken().getTokenSession(), "");
			playRequest.FromJSON(strFromServer);
			printDebugLines(playRequest.ToJSON());
			this.counterPlays++;
			return playRequest;
		} catch (Exception e) {
			return null;
		}
	}
	private void sendCheckAvailabilityResponse() throws IOException {
		CheckAvailabilityResponse request = new CheckAvailabilityResponse(
				this.busy);
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}

	private void sendStopGameResponse() throws IOException {
		StopGameResponse request = new StopGameResponse();
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}

	private void sendStartGameResponse(Token token, BagOfItems bag)
			throws IOException {
		String message = null;
		StartGameResponse request = new StartGameResponse(
				bag.getItemJsonArray(), token.getTokenSession());
		message = request.ToJSON();
		this.busy = true;
		out.println(message);
		this.processState = States.GameStarted;
		printDebugLines(message);
	}

	public void setDebug(boolean var) {
		this.debug = var;
	}

	private void printDebugLines(String message) {
		if (this.debug) {
			System.out.println("Server: " + message);
		}
	}

}
