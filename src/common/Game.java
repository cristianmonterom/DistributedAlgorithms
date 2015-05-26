package common;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Game {
	
	private static Game instance = null;
	
	private int numPlayers;
	private BagOfItems bagItems;
	private BagOfZombies bagZombies;
	private Token token;
	private List<Player> players;
		
	protected Game(int numplayers){
		this.numPlayers = numplayers;
		bagItems = new BagOfItems(numPlayers);
		bagZombies = new BagOfZombies(numplayers * 2);
		token = new Token();
	}
	
	public static Game getInstance(int numPlayers){
		if (instance == null) {
			instance = new Game(numPlayers);
		}
		return instance;
	}
	
	public BagOfItems getBagItems(){
		return this.bagItems;
	}
	
	public Token getToken(){
		return this.token;
	}
	
	public void takenItem(ItemType item){
		this.bagItems.reduceSelected(item);
	}
	
	public Player getWinner(){
		Player tmpPlayer = null;
		try {
			if (this.players.size() > 0) {
				tmpPlayer = this.players.get(0);
			}
			for (int i = 1; i < this.players.size(); i++){
				if (tmpPlayer.getScore() < this.players.get(i).getScore()){
					tmpPlayer = this.players.get(i);
				}
			}
			return tmpPlayer;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean validTurn(String token){
		return this.token.TokenSession.equals(token);
	}
	
	public int getNumPlayers(){
		return this.numPlayers;
	}

	public boolean addPlayer(String name){
		try {
			Player newPlayer = new Player(name);
			this.players.add(newPlayer);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean addPlayerItem(String playerName, Item item){
		try {
			for (int index=0; index< this.players.size(); index++){
				if (this.players.get(index).getName().equals(playerName)){
					this.players.get(index).addItem(item);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public BagOfZombies getBagOfZombies(){
		return this.bagZombies;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getPlayerResultsArray(){
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < this.players.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("player", this.players.get(i).getName());
	      boolean live = false;
	      if (this.bagZombies.getZombiesScore() < this.players.get(i).getScore()) {
	    	  live = true;
	      }
	      formDetailsJson.put("live", String.valueOf(live));
	      jsonArray.add(formDetailsJson);
	    }
		return jsonArray;
	}
	
}
