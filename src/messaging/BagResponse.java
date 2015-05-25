package messaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class BagResponse extends RequestResponse{

	private JSONArray bag;

	public BagResponse(JSONArray bag) {
		this.bag = bag;
	}
	
	public BagResponse() {
	}

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Bag.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("bag", bag);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(_response);
			this.setBag((JSONArray) obj.get("bag"));
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("BagResponse: Message is not valid");
		}	
	}

	public void setBag(JSONArray bag){
		this.bag = bag;
	}

	public JSONArray getBag() {
		return this.bag;
	}
}
