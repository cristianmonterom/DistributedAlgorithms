package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class ResultsResponse extends RequestResponse {
	
	private String winner;
	private JSONArray zombies;
	private int deadZombies;
	
	public ResultsResponse(JSONArray zombies, String winner, int deadZombies) {
		this.zombies = zombies;
		this.winner = winner;
		this.deadZombies = deadZombies;
	}

	public ResultsResponse(){
		
	}
	
	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Results.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("zombies", zombies);
		obj.put("player", winner);
		obj.put("deadZombies", this.deadZombies);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setzombies((JSONArray) obj.get("zombies"));
			try {
				this.winner = obj.get("winner").toString();				
			} catch (Exception e) {
				this.winner = "";
			}
			this.deadZombies = Integer.parseInt(obj.get("deadZombies").toString());
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("ResultsResponse: Message is not valid");
		}	
	}
	
	public void setzombies(JSONArray zombies){
		this.zombies = zombies;
	}
	
	public void setWinner(String winner){
		this.winner = winner;
	}
}
