package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class ResultsResponse extends RequestResponse {
	
//	private String winner;
//	private JSONArray zombies;
//	private int deadZombies;
	private JSONArray players;
	
	public ResultsResponse(JSONArray players) {
//		this.zombies = zombies;
//		this.winner = winner;
		this.players = players;
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
		obj.put("players", players);
//		obj.put("players", winner);
//		obj.put("deadZombies", this.deadZombies);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setPlayers((JSONArray) obj.get("players"));
//			try {
//				this.winner = obj.get("winner").toString();				
//			} catch (Exception e) {
//				this.winner = "";
//			}
//			this.deadZombies = Integer.parseInt(obj.get("deadZombies").toString());
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("ResultsResponse: Message is not valid");
		}	
	}

	public void setPlayers(JSONArray players){
		this.players = players;
	}
	
}
