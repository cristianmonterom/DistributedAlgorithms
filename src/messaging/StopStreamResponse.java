package messaging;

import org.json.simple.JSONObject;
import common.ProtocolMessages;

/**
 * @author Cristian
 *
 */
public class StopStreamResponse extends RequestResponse{
	
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StoppedGame.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), ProtocolMessages.StoppedGame.getValue());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StopStreamResponse: Message is not valid");
		}
	}

}
