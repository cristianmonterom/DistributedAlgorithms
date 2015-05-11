package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class PlayResponse extends RequestResponse {

	private JSONArray bag;
	private String message;
	
	public PlayResponse(JSONArray bag, String message) {
		this.bag = bag;
		this.message = message;
	}

	public PlayResponse() {
		
	}
	
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Play.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("bag", bag);
		obj.put("message", this.message);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setBag((JSONArray) obj.get("bag"));
			this.setMessage(obj.get("message").toString());
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("PlayResponse: Message is not valid");
		}	
	}

	public void setBag(JSONArray bag){
		this.bag = bag;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
}
