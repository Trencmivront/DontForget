package main.io.github.trencmivront.dontforget.enums;

public enum Priority {
	
	NONE(0),
	HIGH(1),
	MEDIUM(2),
	LOW(3);
	
	int value;
	
	private Priority(int value) {
		this.value = value;
	}
	
	public static Priority getPriority(int value) {
		switch (value) {
		case 1: return Priority.HIGH;
		case 2: return Priority.MEDIUM;
		case 3: return Priority.LOW;
		default: return Priority.NONE;
		}
	}
	
	public int getValue() {
		return value;
	}
}
