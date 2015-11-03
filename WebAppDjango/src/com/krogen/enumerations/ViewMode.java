package com.krogen.enumerations;

public enum ViewMode {
	TABLEVIEW("TABLEVIEW"),
	INPUTPANELVIEW("INPUTPANELVIEW");
	
	String label;
	
	ViewMode() {
	}
	
	ViewMode(String label) {
		this.label = label;
	}
}
