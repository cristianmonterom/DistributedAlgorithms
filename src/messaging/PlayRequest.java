package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class PlayRequest extends RequestResponse {
	private JSONArray items;
	
	private String token;
	private String player;
//	private String token_validation;
//	private String player_validation;
	
	public PlayRequest(String token_val, String player_val) {
		this.token = token_val;
		this.player = player_val;
	}

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
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
		obj.put("token", token);
		obj.put("player", player);
		obj.put("items", items);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setToken(obj.get("token").toString());
			this.setPlayer(obj.get("player").toString());
			this.setItems((JSONArray) obj.get("items"));
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("PlayRequest: Message is not valid");
		}	
	}
	
	public void setItems(JSONArray items){
		this.items = items;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public void setPlayer(String player){
		this.player = player;
	}
	
	public JSONArray getItems(){
		return this.items;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public String getPlayer(){
		return this.player;
	}
//	public boolean isValid(){
////		if (this.player.equals(this.player_validation) && this.token.equals(this.token_validation)){
//		if (this.token.equals(this.token_validation)){
//			return true;
//		} else {
//			return false;
//		}
//	}
}
