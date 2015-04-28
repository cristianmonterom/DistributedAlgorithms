package messaging;

import org.json.simple.JSONObject;

import common.CommonFunctions;
import common.ProtocolMessages;

public class StatusResponse extends RequestResponse {
	private String streaming;
	private int totalClients;
	private String rateLimit;
	private String handover;

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
	}

	@Override
	String Action() {
		return ProtocolMessages.Status.getValue();
	}

	public StatusResponse() {

	}

	public StatusResponse(boolean mode, int totalClients, boolean rateLimit,
			boolean handover) {
		this.streaming = mode ? "local" : "remote";
		this.totalClients = totalClients;
		this.rateLimit = CommonFunctions.convertBoolean(rateLimit);
		this.handover = CommonFunctions.convertBoolean(handover);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(Type(), Action());
		obj.put("streaming", this.streaming);
		obj.put("clients", this.totalClients);
		obj.put("ratelimiting", this.rateLimit);
		obj.put("handover", this.handover);

		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			System.err.println("StatusResponse: Message is not valid");
		}
		
		try {
			this.streaming = (String) obj.get("streaming");
			this.totalClients = (Integer.parseInt(obj.get("clients").toString()));
			this.rateLimit = (String) obj.get("ratelimiting");
			this.handover = (String) obj.get("handover");
		} catch (Exception e) {
			System.err.println("StatusResponse: Message format is not valid");
		}
	}

}
