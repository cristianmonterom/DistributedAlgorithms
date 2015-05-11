package common;

public enum ZombieType {
	Atype(1, "a-type", 50, "a-type.jpg"), Btype(2, "b-type", 100, "b-type.jpg");
	private int id;
	private String name;
	private int value;
	private String img;
	
	ZombieType(int id, String name, int value, String img) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.img = img;
	}
	
	public String getImagePath() {
		return img;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.getName() + ' ' + String.valueOf(this.getValue());
	}

	public static ZombieType getById(int id) {
		for (ZombieType e : values()) {
			if (e.id == id)
				return e;
		}
		return null;
	}

	public static ZombieType getByName(String name) {
		for (ZombieType e : values()) {
			if (e.name.equals(name))
				return e;
		}
		return null;
	}
}
