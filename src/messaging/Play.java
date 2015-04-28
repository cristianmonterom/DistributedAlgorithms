package messaging;

import org.json.simple.JSONObject;

import common.ProtocolMessages;

public class Play extends RequestResponse {
	String imageData;
	boolean correctMessage = true;

	public Play(String strDataToSend) {
		this.imageData = strDataToSend;
	}

	public Play() {

	}

	@Override
	String Type() {
		return ProtocolMessages.Response.getValue();
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
		obj.put(ProtocolMessages.Play.getValue(), this.imageData);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String _response) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(_response);
		} catch (org.json.simple.parser.ParseException e) {
			this.imageData = "";
			this.correctMessage=false;
		}

		try {
			this.imageData = (String) obj.get("data");
			if (this.imageData.isEmpty()){
				this.correctMessage=false;
			}
		} catch (Exception e) {
			this.imageData="";
			this.correctMessage=false;
		}

	}

	public String getImageData() {
		return imageData;
	}

	public boolean isCorrectMessage() {
		return correctMessage;
	}
}
