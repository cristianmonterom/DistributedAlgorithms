package common;

import java.util.Calendar;
import java.util.UUID;

public class Token {
	String TokenSession;
	
	public Token(){
		Calendar cal = Calendar.getInstance();
		this.TokenSession = UUID.randomUUID().toString().toUpperCase() 
	            + cal.getTimeInMillis();	
	}
	
	public boolean equals(String token){
		return this.TokenSession == token;
	}
	
	public String getTokenSession(){
		return this.TokenSession;
	}
}
