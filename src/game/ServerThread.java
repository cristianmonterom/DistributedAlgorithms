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

import messaging.BagRequest;
import messaging.BagResponse;
import messaging.CheckAvailabilityRequest;
import messaging.CheckAvailabilityResponse;
import messaging.PlayRequest;
import messaging.PlayResponse;
import messaging.RequestResponseFactory;
import messaging.ResultsRequest;
import messaging.ResultsResponse;
import messaging.StartGameRequest;
import messaging.StartGameResponse;
import messaging.StopGameRequest;
import messaging.StopGameResponse;
import common.BagOfItems;
import common.Game;
import common.Item;
import common.ItemType;
import common.Player;
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
	private String player;

	public ServerThread(Socket socket) {
		processState = States.StandBy;

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
		while (this.processState != States.End) {
			try {
				do {
					strFromServer = in.readLine();
				} while (strFromServer.isEmpty());
			} catch (IOException e1) {
				this.printDebugLines("Error while reading: " + e1.getStackTrace().toString());
//				System.out.println("out");
//				String error = e1.getMessage();
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
						newGame = Game.getInstance(players);
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
//					System.out.println("qqqqq---" + strFromServer);
					switch (RequestResponseFactory.getStateMessage(
							this.processState, strFromServer)) {
					case CheckAvailability:
						receiveCheckAvailabilityRequest(strFromServer);
						sendCheckAvailabilityResponse();
						break;
					case Play:
						PlayRequest pr = receivePlayRequest(strFromServer);
						if (!this.newGame.getBagItems().isEmpty()){
//							if (this.counterPlays < this.newGame.getNumPlayers()){
							String message = this.newGame.validTurn(pr.getToken()) ? "" : "Incorrect Token";
							JSONArray items = pr.getItems();
							JSONParser parser = new JSONParser();
							this.newGame.addPlayer(pr.getPlayer());
							for (int i = 0; i< items.size(); i++){
								JSONObject obj = (JSONObject) parser.parse(items.get(i).toString());
								ItemType newItemType = ItemType.getByName(obj.get("item").toString());
								int amount = 1;
								try {
									amount = Integer.parseInt(obj.get("amount").toString());
								} catch (Exception e) {
									amount = 1;
								}
								Item newItem = new Item(newItemType, amount);
								this.newGame.takenItem(newItemType);
								this.newGame.addPlayerItem(pr.getPlayer(), newItem);
							}
							sendPlayResponse(this.newGame.getBagItems().getItemJsonArray(), message);
						} else {
//						if (this.counterPlays == this.newGame.getNumPlayers()) {
							sendResultsResponse();
							this.processState = States.GameOver;
						}
						break;
					case Results:
						receiveResultsRequest(strFromServer);
						sendResultsResponse();
						this.processState = States.GameOver;
						break;
					case Bag:
						receiveBagRequest(strFromServer);
						sendBagResponse(this.newGame.getBagItems());
						break;
					default:
						printDebugLines("Invalid message type in state: " + this.processState);
						break;
					}
					break;
				case GameOver:
					switch (RequestResponseFactory.getStateMessage(
							this.processState, strFromServer)) {
					case CheckAvailability:
						receiveCheckAvailabilityRequest(strFromServer);
						sendCheckAvailabilityResponse();
						break;
					case Results:
						receiveResultsRequest(strFromServer);
						sendResultsResponse();
						break;
					case StopGame:
						 receiveStopGameRequest(strFromServer);
						 sendStopGameResponse();
						 this.processState = States.End;
						break;
					default:
						break;
					}
				default:
					break;
				}

			} catch (Exception e) {
//				String error = e.getMessage();
				 printDebugLines(e.getStackTrace().toString());
			}
		}
	}

	private void receiveResultsRequest(String strFromServer) {
		try {
			ResultsRequest resultsRequest = new ResultsRequest();
			resultsRequest.FromJSON(strFromServer);
			this.player = resultsRequest.getPlayer();
			printDebugLines(resultsRequest.ToJSON());
		} catch (Exception e) {
		}
	}

	private void sendResultsResponse() {
//		Player p = this.newGame.getWinner();
//		String sPlayer = p == null ? "" : p.getName(); 
		ResultsResponse response = new ResultsResponse(this.newGame.getPlayerResultsArray());
		out.println(response.ToJSON());
		printDebugLines(response.ToJSON());
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

	private boolean receiveBagRequest(String strFromServer) {
		try {
			BagRequest bagRequest;
			bagRequest = new BagRequest();
			bagRequest.FromJSON(strFromServer);
			printDebugLines(bagRequest.ToJSON());
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
		try {
			this.busy = this.newGame.isNull() ? false : true;			
		} catch (Exception e) {
			this.busy = false;
		}
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

	private void sendBagResponse(BagOfItems bag) throws IOException {
		String message = null;
		BagResponse response = new BagResponse(bag.getItemJsonArray());
		message = response.ToJSON();
		out.println(message);
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
