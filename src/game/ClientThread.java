package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
	private int numPlayers;
	
	private boolean debug = false;

	public ClientThread(Socket socket) {
		socketRef = socket;
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		receiveCheckAvailabilityResponse();
		
		try {
			sendStartGameRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiveStartGameResponse();

//		do {
//			receiveBag();
//		} while (keyboardCapture.isRunning());

		sendStopgameRequest();

		receiveStopGameResponse();

//		out.close();
//		try {
//			in.close();
//			socketRef.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}

		System.exit(-1);
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
		
		printDebugLines(startGameResponse.ToJSON());
	}

	private void sendStartGameRequest() throws IOException {
		StartGameRequest request = new StartGameRequest(this.numPlayers);
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
		Play streamMsgFromServer;

		try {
			streamMsgFromServer = new Play();
			streamMsgFromServer.FromJSON(streamStr);

			if (streamMsgFromServer.isCorrectMessage()) {
				out.println("received message " + streamMsgFromServer.ToJSON());
			} else {
				StopStreamResponse stopStreamResp = new StopStreamResponse();
				stopStreamResp.FromJSON(streamStr);
			}

		} catch (Exception e) {
			printDebugLines(e.getStackTrace().toString());
			StopStreamResponse stopStreamResponse = new StopStreamResponse();
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
}
