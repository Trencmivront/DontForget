package app.enums;

import java.awt.Color;

public enum LightColors {
	
	BUTTON_HOVER(226, 232, 240),
	PRIMARY(42, 157, 143), // changed
	PRIMARY_HOVER(79, 70, 229),
	BACKGROUND(38, 70, 83), // changed
	SIDEBAR(241, 245, 249),
	TEXT_MAIN(15, 23, 42),
	TEXT_SECONDARY(71, 85, 105),
	TEXT_MUTED(148, 163, 184),
	BORDER(233, 196, 106),// changed
	SUCCESS(16, 185, 129),
	WARNING(245, 158, 11),
	DANGER(239, 68, 68),
	INFO(59, 130, 246),
	TASK_HIGH(231, 111, 81), // changed
	TASK_MEDIUM(244, 162, 97),// changed
	TASK_LOW(59, 130, 246),
	TASK_COMPLETED(16, 185, 129);
	
	private int red;
	private int green;
	private int blue;
	
	private LightColors(int red , int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color getColor() {
		return new Color(red, green, blue);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	

}
