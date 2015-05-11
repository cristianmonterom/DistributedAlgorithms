/**
 * 
 */
package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

/**
 * @author Cristian
 *
 */
public class StartGameRequest extends RequestResponse {

	private int numPlayers;
	
	public StartGameRequest(){}
	
	public StartGameRequest(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StartGame.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("numPlayers", this.numPlayers);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StartGameRequest: Message is not valid");
		}
		
		this.numPlayers = Integer.parseInt(obj.get("numPlayers").toString());
	}

	public int getNumPlayers() {
		return numPlayers;
	}
}
