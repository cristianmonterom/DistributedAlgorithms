package game;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Connection {
	protected ServerSocket server;
    protected Socket socket;  
    
    public abstract Socket establishConnection();	
}