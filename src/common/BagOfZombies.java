package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BagOfZombies {
	protected static final JSONParser parser = new JSONParser();
	private List<Zombie> zombies;
	
	public BagOfZombies(int numZombies) {
		this.zombies = new ArrayList<Zombie>();
		this.generateInitialBagOfZombies(numZombies);
	}

	private void generateInitialBagOfZombies(int numZombies) {
		int numExistingZombies = ZombieType.values().length;
		int maxZombies = (numExistingZombies / 2) * numZombies;
		
		for (int i = 0; i < numExistingZombies; i++){
			this.zombies.add(new Zombie(ZombieType.getById(i+1), 1));
		}
		
		maxZombies -= numExistingZombies;
		int index = 0;
		for (int i = maxZombies; i > 0; i--){
			index = randInt(0, zombies.size() -1);
			zombies.get(index).addAmount();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String ToJSON(){
		JSONObject obj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < this.zombies.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("zombie", this.zombies.get(i).getZombie().getName());
	      formDetailsJson.put("amount", this.zombies.get(i).getAmount());
	      formDetailsJson.put("img", this.zombies.get(i).getZombie().getImagePath());
	      
	      jsonArray.add(formDetailsJson);
	    }
		obj.put("zombies", jsonArray);
		
		return obj.toJSONString();
	}
	
	private int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	public boolean reduceSelected(ZombieType zombie){
		for (int i=0; i< this.zombies.size(); i++){
			if (this.zombies.get(i).getZombie().getName().equals(zombie.getName())){
				this.zombies.get(i).removeAmount();
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getZombieJsonArray(){
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < this.zombies.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("zombie", this.zombies.get(i).getZombie().getName());
	      formDetailsJson.put("amount", this.zombies.get(i).getAmount());
	      formDetailsJson.put("img", this.zombies.get(i).getZombie().getImagePath());
	      jsonArray.add(formDetailsJson);
	    }
		return jsonArray;
	}
	
	public int getZombiesScore(){
		int result = 0;
		for (int i=0; i< this.zombies.size(); i++){
			result = result + (this.zombies.get(i).getAmount() * this.zombies.get(i).getZombie().getValue());
		}
		return result;
	}
	
}
