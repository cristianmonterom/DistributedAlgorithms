package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class ResultsResponse extends RequestResponse {
	private String winner;
	
	public ResultsResponse(String winner) {
		this.winner = winner;
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
		obj.put("player", winner);
		return obj.toJSONString();
	}

	@SuppressWarnings("unused")
	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.winner = obj.get("winner").toString();
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("ResultsResponse: Message is not valid");
		}	
	}
}
