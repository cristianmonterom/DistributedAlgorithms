package common;

public enum Constants {
	PORT(6262);

	private int value;

	private Constants(int setValue) {
		value = setValue;
	}

	public int getValue() {
		return value;
	}
}
