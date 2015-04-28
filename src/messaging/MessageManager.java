package messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import common.ProtocolMessages;

public class MessageManager {
	
	private PrintWriter out;
	private BufferedReader in;

	MessageManager(BufferedReader dIs, PrintWriter dOs)
	{
		in = dIs;
		out = dOs;
	}
	
	public void doSomething(){
		String msgReceived = null;
		String msgToSend = null;
		RequestResponseFactory req_resp_message = new RequestResponseFactory();
		
		try {
			msgReceived = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RequestResponse messageReceived = req_resp_message.FromJSON(msgReceived);
		msgToSend = ProccessMessage(messageReceived);
		
		if(msgToSend!=null){
			out.println(msgToSend);
		}
	}
	
	public static String ProccessMessage(RequestResponse message){
		String retValue = null;
//		if(message.Type().equals(ProtocolMessages.Request.getValue())){
//			if(message.Action().equals(ProtocolMessages.StopStream.getValue())){
//				StopStreamResponse resp = new StopStreamResponse();
//				retValue = resp.ToJSON();
//			}
//		}
		return retValue;
	}

}
