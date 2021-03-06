package common;

public enum ItemType {
	Knife(1, "knife", 50, "knife.jpg"), Gun(2, "gun", 100, "gun.jpg"), AidBox(
			3, "aidbox", 75, "aidbox.jpg");
	private int id;
	private String name;
	private int value;
	private String img;

	ItemType(int id, String name, int value, String img) {
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

	public static ItemType getById(int id) {
		for (ItemType e : values()) {
			if (e.id == id)
				return e;
		}
		return null;
	}

	public static ItemType getByName(String name) {
		for (ItemType e : values()) {
			if (e.name.equals(name))
				return e;
		}
		return null;
	}
}
