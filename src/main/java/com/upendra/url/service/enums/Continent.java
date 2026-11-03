package com.upendra.url.service.enums;

public enum Continent {
	ASIA("Asia"), EUROPE("Europe"), AFRICA("Africa"), NORTH_AMERICA("North America"), SOUTH_AMERICA("South America"),
	AUSTRALIA("Australia"), ANTARCTICA("Antarctica");

	private final String label;

	Continent(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
