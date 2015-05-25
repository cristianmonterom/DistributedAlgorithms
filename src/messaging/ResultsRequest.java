package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class ResultsRequest extends RequestResponse {

	private String player;
	
	public ResultsRequest(String player) {
		this.player = player;
	}
	
	public ResultsRequest(){}

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
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
		obj.put("player", player);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setPlayer(obj.get("player").toString());
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("ResultsRequest: Message is not valid");
		}	
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}
	
}
