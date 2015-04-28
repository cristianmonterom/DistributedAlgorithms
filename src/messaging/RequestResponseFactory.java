package messaging;

import common.ProtocolMessages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author santiago
 *
 */

public class RequestResponseFactory {
	
	private static final JSONParser parser = new JSONParser();
	
	public RequestResponseFactory(){}
	
	
	public RequestResponse FromJSON(String msgStr){
		JSONObject obj;
		
		try{
			obj = (JSONObject) parser.parse(msgStr);
		}catch(ParseException ex){
			ex.printStackTrace();
			return null;
		}
		
		if(obj !=null){
			RequestResponse reqResMsg = null;
			
//			if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.Status.getValue())){
//				reqResMsg = new StatusResponse();
//			}
//			else if(obj.get(ProtocolMessages.Request.getValue()).equals(ProtocolMessages.StartSream.getValue())){
//				reqResMsg = new StartStreamRequest();
//			}
//			else if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.StartingStream.getValue())){
//				reqResMsg = new StartStreamResponse();
//			}
//			
//			else if(obj.get(ProtocolMessages.Request.getValue()).equals(ProtocolMessages.StopStream.getValue())){
//				reqResMsg = new StopStreamRequest();
//			}
//			else if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.StoppedStream.getValue())){
//				reqResMsg = new StopStreamResponse();
//			}
//			else if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.Image.getValue())){
//				reqResMsg = new Stream();
//			}
//			if(obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.Overloaded.getValue())){
//				reqResMsg = new OverloadResponse();
//				reqResMsg.FromJSON(msgStr);
//			}
			

			return reqResMsg;
		}else return null;
	}
}
