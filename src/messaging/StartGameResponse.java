package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class StartGameResponse extends RequestResponse {
	
	private JSONArray bag;
	private String token;
	
	public StartGameResponse(JSONArray bag, String token) {
		this.bag = bag;
		this.token = token;
	}
	
	public StartGameResponse(){
		
	}
	
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StartingGame.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("bag", bag);
		obj.put("token", token);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setBag((JSONArray) obj.get("bag"));
			this.setToken(obj.get("token").toString());
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StartGameResponse: Message is not valid");
		}	
	}

	public void setBag(JSONArray bag){
		this.bag = bag;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getToken(){
		return this.token;
	}

	public JSONArray getBag(){
		return this.bag;
	}
}
