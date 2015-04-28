package game;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import common.CommonFunctions;
import common.Constants;

public class ArgumentParser {
	@Option(name = "-port", usage = "Sets server port")
	private int serverPort;

	@Option(name = "-server", usage = "Sets remote IP to connect to")
	private String server = null;

	@Option(name = "-clientport", usage = "Sets client port")
	private int clientPort;

	@Option(name = "-debug", usage = "Sets if is in debugging mode or not")
	private String debug;

	public void Parse(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("--------Invalid arguments---------");
			System.exit(1);
		}

		if (!server.isEmpty()) {
			if (!CommonFunctions.isPortValid(serverPort)) {
				serverPort = Constants.PORT.getValue();
			}
		}

		if (!CommonFunctions.isPortValid(clientPort)) {
			clientPort = Constants.PORT.getValue();
		}
		
		if (!(debug.toLowerCase().equals("true") || debug.toLowerCase().equals("false"))){
			debug = "false";
		}

	}

	public ArgumentParser() {
		serverPort = Constants.PORT.getValue();
		clientPort = Constants.PORT.getValue();
		server = "";
		debug = "false";
	}

	public int getServerPort() {
		if (CommonFunctions.isPortValid(serverPort)) {
			return serverPort;
		} else {
			return Constants.PORT.getValue();
		}
	}

	public int getClientPort() {
		if (CommonFunctions.isPortValid(clientPort)) {
			return clientPort;
		} else {
			return Constants.PORT.getValue();
		}
	}
	
	public String getHostname() {
		return server;
	}
	
	public boolean isDebugging(){
		return this.debug.toLowerCase().equals("true") ? true : false;
	}

}
