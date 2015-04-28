package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class CheckAvailabilityResponse extends RequestResponse {
	
	private boolean busy;
	public CheckAvailabilityResponse(boolean busy){
		this.busy = busy;
	}
	
	public CheckAvailabilityResponse(){
		
	}

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
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
		obj.put("busy", String.valueOf(this.busy).toLowerCase());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		@SuppressWarnings("unused")
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("CheckAvailabilityResponse: Message is not valid");
		}
	}
}
