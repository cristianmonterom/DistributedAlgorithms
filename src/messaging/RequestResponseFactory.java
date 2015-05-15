package messaging;

import common.ProtocolMessages;
import common.States;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author cristian
 *
 */

public class RequestResponseFactory {

	private static final JSONParser parser = new JSONParser();

	public RequestResponseFactory() {
	}

	public static RequestResponse FromJSON(String msgStr) {
		JSONObject obj;

		try {
			obj = (JSONObject) parser.parse(msgStr);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}

		if (obj != null) {
			RequestResponse reqResMsg = null;

			if (obj.get(ProtocolMessages.Response.getValue()).equals(
					ProtocolMessages.Status.getValue())) {
				reqResMsg = new StatusResponse();
			} else if (obj.get(ProtocolMessages.Request.getValue()).equals(
					ProtocolMessages.CheckAvailability.getValue())) {
				reqResMsg = new CheckAvailabilityRequest();
			}
			// else
			// if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.StartingStream.getValue())){
			// reqResMsg = new StartStreamResponse();
			// }
			//
			// else
			// if(obj.get(ProtocolMessages.Request.getValue()).equals(ProtocolMessages.StopStream.getValue())){
			// reqResMsg = new StopStreamRequest();
			// }
			// else
			// if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.StoppedStream.getValue())){
			// reqResMsg = new StopStreamResponse();
			// }
			// else
			// if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.Image.getValue())){
			// reqResMsg = new Stream();
			// }
			// if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.Overloaded.getValue())){
			// reqResMsg = new OverloadResponse();
			// reqResMsg.FromJSON(msgStr);
			// }

			return reqResMsg;
		} else
			return null;
	}

	public static ProtocolMessages getStateMessage(States appState, String message) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(message);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		if (obj != null) {
			switch (appState) {
			case StandBy:
				if (obj.get(ProtocolMessages.Request.getValue()).equals(
						ProtocolMessages.CheckAvailability.getValue())) {
					return ProtocolMessages.CheckAvailability;
				}
				if (obj.get(ProtocolMessages.Request.getValue()).equals(
								ProtocolMessages.StartGame.getValue())) {
					return ProtocolMessages.StartGame;
				} 
			case GameStarted:
				if (obj.get(ProtocolMessages.Request.getValue()).equals(
						ProtocolMessages.Bag.getValue())) {
					return ProtocolMessages.Bag;
				}
				if (obj.get(ProtocolMessages.Request.getValue()).equals(
						ProtocolMessages.Play.getValue())) {
					return ProtocolMessages.Play;
				}
				if (obj.get(ProtocolMessages.Request.getValue()).equals(
						ProtocolMessages.StopGame.getValue())) {
					return ProtocolMessages.StopGame;
				}
			
			default:
				return null;

			}
		}
		return null;
	}

}
