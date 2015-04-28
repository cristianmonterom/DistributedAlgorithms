package messaging;

import org.json.simple.JSONObject;
import common.ProtocolMessages;

public class CheckAvailabilityRequest extends RequestResponse {

	public CheckAvailabilityRequest(){}

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.CheckAvailability.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), ProtocolMessages.CheckAvailability.getValue());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("CheckAvailabilityRequest: Message is not valid");
		}
	}

}
