package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.Item;
import common.ItemType;
import common.ProtocolMessages;
import messaging.*;

/**
 * 
 * @author Cristian
 *
 */
public class ClientThread implements Runnable {

	private PrintWriter out;
	private BufferedReader in;
	StartGameResponse startStreamResponse;
	Thread keyboardThread;
	KeyboardCapture keyboardCapture;

	private OutputStream outputStream;
	private InputStream inputStream;
	private Socket socketRef;
	private int numPlayers = 1;
	
	private boolean debug = false;
	private boolean busyServer = false;
	private String token;

	public ClientThread(Socket socket) {
		this.socketRef = socket;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));
	}

	public void run() {
//		keyboardThread.start();

		try {
			sendCheckAvailabilityRequest();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		receiveCheckAvailabilityResponse();

		if (!this.busyServer){
			try {
				sendStartGameRequest();
			} catch (IOException e) {
				e.printStackTrace();
			}
			receiveStartGameResponse();

			try {
				sendPlayRequest();
			} catch (IOException e) {
				e.printStackTrace();
			}

			receivePlayResponse();
			try{
				sendBagRequest();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			receiveBagResponse();
			
			receiveResultsResponse();
			sendStopgameRequest();
			receiveStopGameResponse();
		}
		


		out.close();
		try {
			in.close();
//			socketRef.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.exit(-1);
	}
	
	private void receivePlayResponse() {
		String strFromServer = null;
		PlayResponse playResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		playResponse = new PlayResponse();
		playResponse.FromJSON(strFromServer);
		
		printDebugLines(playResponse.ToJSON());
	}

	private void receiveResultsResponse() {
		String strFromServer = null;
		ResultsResponse resultsResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		resultsResponse = new ResultsResponse();
		resultsResponse.FromJSON(strFromServer);
		
		printDebugLines(resultsResponse.ToJSON());
	}

	private void sendCheckAvailabilityRequest() throws IOException {
		CheckAvailabilityRequest request = new CheckAvailabilityRequest();
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}

	private void receiveCheckAvailabilityResponse() {
		String strFromServer = null;
		CheckAvailabilityResponse checkAvailabilityResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		checkAvailabilityResponse = new CheckAvailabilityResponse();
		checkAvailabilityResponse.FromJSON(strFromServer);
		this.busyServer = checkAvailabilityResponse.getBusy();
		
		printDebugLines(checkAvailabilityResponse.ToJSON());
	}
	
	private void receiveStartGameResponse() {
		String strFromServer = null;
		StartGameResponse startGameResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		startGameResponse = new StartGameResponse();
		startGameResponse.FromJSON(strFromServer);
		this.token = startGameResponse.getToken();
		printDebugLines(startGameResponse.ToJSON());
	}

	private void receiveBagResponse() {
		String strFromServer = null;
		BagResponse bagResponse = new BagResponse();
		try {
			strFromServer = in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		bagResponse.FromJSON(strFromServer);
		printDebugLines(bagResponse.ToJSON());
	}
	
	private void sendBagRequest() throws IOException {
		BagRequest request = new BagRequest();
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}
	
	private void sendStartGameRequest() throws IOException {
		StartGameRequest request = new StartGameRequest(this.numPlayers);
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}

	private void sendPlayRequest() throws IOException {
		PlayRequest request = new PlayRequest(this.token, InetAddress.getLocalHost().toString());
		request.setItems(someItems());
		out.println(request.ToJSON());
		printDebugLines(request.ToJSON());
	}
	
	
	private void sendStopgameRequest() {
		StopGameRequest stopGameReq = new StopGameRequest();
		String str = stopGameReq.ToJSON();
		out.println(str);
		printDebugLines(str);
	}

	private void receiveStopGameResponse() {
		String strFromServer = null;

		JSONObject obj = null;
		final JSONParser parser = new JSONParser();

		do {
			try {
				strFromServer = in.readLine();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			try {
				obj = (JSONObject) parser.parse(strFromServer);
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
		} while (!obj.get(ProtocolMessages.Response.getValue()).equals(
				ProtocolMessages.StoppedGame.getValue()));
		printDebugLines(obj.toJSONString());

	}

	private void receiveBag() {
		String streamStr = readMessage();
//		Play streamMsgFromServer;

		try {
//			streamMsgFromServer = new Play();
//			streamMsgFromServer.FromJSON(streamStr);
//
//			if (streamMsgFromServer.isCorrectMessage()) {
//				out.println("received message " + streamMsgFromServer.ToJSON());
//			} else {
//				StopGameResponse stopStreamResp = new StopGameResponse();
//				stopStreamResp.FromJSON(streamStr);
//			}

		} catch (Exception e) {
			printDebugLines(e.getStackTrace().toString());
			StopGameResponse stopStreamResponse = new StopGameResponse();
			stopStreamResponse.FromJSON(streamStr);
		}
		printDebugLines(streamStr);
	}
	
	private String readMessage(){
		String streamStr = null;
		try {
			do {
				streamStr = in.readLine();
			} while (streamStr.isEmpty());
		} catch (IOException e) {
			this.printDebugLines(e.getStackTrace().toString());
		}	
		this.printDebugLines(streamStr);
		return streamStr;
	}
	
	public void setDebug(boolean var){
		this.debug = var;
	}

	private void printDebugLines(String message){
		if (this.debug){
			System.out.println(message);
		}		
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray someItems(){
		int numExistingItems = ItemType.values().length;
		
		List<Item> items = new ArrayList<Item>();
		Random rand = new Random(); 
		int x = rand.nextInt(numExistingItems);
		items.add(new Item(ItemType.getById(x+1), 1));
		
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < items.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("item", items.get(i).getItem().getName());
	      formDetailsJson.put("amount", items.get(i).getAmount());
	      jsonArray.add(formDetailsJson);
	    }
		return jsonArray;
	}
}
