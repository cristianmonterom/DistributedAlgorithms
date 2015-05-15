package common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Item {
	protected static final JSONParser parser = new JSONParser();
	private int amount;
	private ItemType item;
	
	public Item(ItemType item, int amount){
		this.item = item;
		this.amount = amount;
	}
	
	public Item(JSONObject item){
		this.item = ItemType.getByName(item.get("item").toString());
		this.amount = Integer.parseInt(item.get("amount").toString());
	}
	
	@SuppressWarnings("unchecked")
	public String ToJSON(){
		JSONObject obj = new JSONObject();
		obj.put("item", this.item.getName());
		obj.put("amount", this.amount);
		return obj.toJSONString();
	}
	
	public ItemType getItem(){
		return this.item;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void addAmount(){
		this.setAmount(amount + 1);
	}
	
	public void removeAmount(){
		this.setAmount(amount - 1);
	}
	
	
}
