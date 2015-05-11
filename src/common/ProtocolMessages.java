package common;

public enum ProtocolMessages {
	Status("status"), StartGame("startgame"), StartingGame(
			"startinggame"), StopGame("stopgame"), StoppedGame(
			"stoppedgame"), SelectedItems("selecteditems"), 
			StoredItems("storeditems"), Results("results"),
			Request("request"), Response("response"),
			Play("play"), TakenServer("takenserver"),
			CheckAvailability("checkavailability")
			;

	private String value;

	ProtocolMessages(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}
