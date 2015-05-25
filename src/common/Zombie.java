package common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Zombie {
	protected static final JSONParser parser = new JSONParser();
	private int amount;
	private ZombieType zombie;
	
	public Zombie(ZombieType zombie, int amount) {
		this.zombie = zombie;
		this.amount = amount;
	}
	
	public Zombie(JSONObject zombie){
		this.zombie = ZombieType.getByName(zombie.get("zombie").toString());
		this.amount = Integer.parseInt(zombie.get("amount").toString());
	}

	@SuppressWarnings("unchecked")
	public String ToJSON(){
		JSONObject obj = new JSONObject();
		obj.put("zombie", this.zombie.getName());
		obj.put("amount", this.amount);
		return obj.toJSONString();
	}
	
	public ZombieType getZombie(){
		return this.zombie;
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
		this.setAmount(amount -1);
	}
}
