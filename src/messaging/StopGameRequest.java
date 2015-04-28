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
public class StopGameRequest extends RequestResponse {

	@Override
	String Type() {
		return ProtocolMessages.Request.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.StopGame.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());

		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StopStreamRequest: Message is not valid");
		}
	}

}
