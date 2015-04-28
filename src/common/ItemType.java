package common;

public enum ItemType {
	Knife(1, "knife", 50),
	Gun(2, "gun", 100),
	AidBox(3, "aidbox", 75)
	;
	private int id;
	private String name;
	private int value;

	ItemType(int id, String name, int value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getName(){
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getId(){
		return id;
	}

	@Override
	public String toString() {
		return this.getName() + ' ' + String.valueOf(this.getValue());
	}
	public static ItemType getById(int id) {
	    for(ItemType e : values()) {
	        if(e.id == id) return e;
	    }
	    return null;
	 }
}
