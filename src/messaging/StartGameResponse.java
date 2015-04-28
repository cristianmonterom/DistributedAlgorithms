package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class StartGameResponse extends RequestResponse {
	
	private String bag;
	private String token;
	
	public StartGameResponse(String bag, String token) {
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

	@SuppressWarnings("unused")
	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			
			this.setBag(obj.get("bag").toString());
			this.setToken(obj.get("token").toString());
			
			System.out.println("asdfasfds" + _response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StartGameResponse: Message is not valid");
		}	
	}

	public void setBag(String bag){
		this.bag = bag;
	}
	
	public void setToken(String token){
		this.token = token;
	}

}
