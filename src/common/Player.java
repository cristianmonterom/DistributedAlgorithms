package common;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Player {
	private int id;
	private String name;
	private List<Item> items;


	public Player(String name) {
		this.name = name;
		this.items = new ArrayList<Item>();
	}
	
	public boolean addItem(Item item){
		try {
			this.items.add(item);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getScore(){
		int result = 0;
		for (int i = 0; i < this.items.size(); i++){
			result += (this.items.get(i).getAmount() * this.items.get(i).getItem().getValue());
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public JSONObject getJsonObject(){
		JSONObject obj = null;
		obj.put("name", this.name);
		obj.put("score", getScore());
		return obj;
	}
		
}
