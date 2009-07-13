package com.n4systems.fieldid.datatypes;

public class ButtonGroup {

	String name;
	boolean setsResult = false;
	
	public ButtonGroup(String name) {
		this.name = name;
	}

	public void setButtonGroup(String name) {
		this.name = name;
	}
	
	public String getButtonGroup() {
		return name;
	}
	
	public boolean getSetsResults() {
		return setsResult;
	}
	
	public void setSetsResult(boolean b) {
		setsResult = b;
	}
}
