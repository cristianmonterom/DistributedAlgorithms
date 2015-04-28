package common;

public class Game {
	private int numPlayers;
	private String leaderPlayer;
	private String currentPlayer;
	private String NextPlayer;
	private BagOfItems bagItems;
	private Token token;
	
	public Game(int numplayers){
		this.numPlayers = numplayers;
		bagItems = new BagOfItems(numPlayers);
		token = new Token();
	}
	
	public BagOfItems getBagItems(){
		return this.bagItems;
	}
	
	public Token getToken(){
		return this.token;
	}

}
