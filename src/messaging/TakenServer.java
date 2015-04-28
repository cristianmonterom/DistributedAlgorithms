package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class TakenServer extends RequestResponse{
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.TakenServer.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), ProtocolMessages.TakenServer.getValue());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("TakenServer: Message is not valid - " + _response);
		}
	}
}
