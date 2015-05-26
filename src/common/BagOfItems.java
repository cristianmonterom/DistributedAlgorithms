package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BagOfItems {
	protected static final JSONParser parser = new JSONParser();
	private List<Item> items;
	
	public BagOfItems(int numPlayers){
		this.items = new ArrayList<Item>();
		this.generateInitialBag(numPlayers);
	}

	private void generateInitialBag(int numPlayers){
		int numExistingItems = ItemType.values().length;
//		int maxItems = (numExistingItems / 2) * numPlayers;
		
		// add one of each item at the beginning
		for (int i = 0; i < numExistingItems; i++){
			this.items.add(new Item(ItemType.getById(i+1), 1));
		}
		
		// add items at random until maxitems is reached
//		maxItems -= numExistingItems;
//		int index = 0;
//		for (int i = maxItems; i > 0; i--){
//			index = randInt(0, items.size() - 1);
//			items.get(index).addAmount();
//		}
	}
	
	@SuppressWarnings("unchecked")
	public String ToJSON(){
		JSONObject obj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		for (int i = 0; i < this.items.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("item", this.items.get(i).getItem().getName());
	      formDetailsJson.put("amount", this.items.get(i).getAmount());
	      formDetailsJson.put("img", this.items.get(i).getItem().getImagePath());
	      
	      jsonArray.add(formDetailsJson);
	    }
		obj.put("items", jsonArray);
		
		return obj.toJSONString();
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public boolean reduceSelected(ItemType item){
		for (int i=0; i< this.items.size(); i++){
			if (this.items.get(i).getItem().getName().equals(item.getName())){
				this.items.get(i).removeAmount();
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getItemJsonArray(){
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < this.items.size(); i++)
	    {
	      JSONObject formDetailsJson = new JSONObject();
	      formDetailsJson.put("item", this.items.get(i).getItem().getName());
	      formDetailsJson.put("amount", this.items.get(i).getAmount());
	      formDetailsJson.put("img", this.items.get(i).getItem().getImagePath());
	      jsonArray.add(formDetailsJson);
	    }
		return jsonArray;
	}
	
	public boolean isEmpty() {
		int total = 0;
		for (int i = 0; i < this.items.size(); i++) {
			total += this.items.get(i).getAmount();
	    }
		return total == 0 ? true : false;
	}
	
}
