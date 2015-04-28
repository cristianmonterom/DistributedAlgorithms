package messaging;

import org.json.simple.parser.JSONParser;


public abstract class RequestResponse {
	protected static final JSONParser parser = new JSONParser();
	
	abstract String Type();
	abstract String Action();
	public abstract String ToJSON();

	public abstract void FromJSON(String _response);

}
