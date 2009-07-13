package com.n4systems.fieldid.testcase;

public class ButtonGroup {

	String option = "Pass, Fail";
	boolean setsResult = true;
	
	public ButtonGroup() {
		super();
	}

	public ButtonGroup(String bg, boolean setsResult) {
		super();
		option = bg;
		this.setsResult = setsResult;
	}
	
	public String getButtonGroup() {
		return option;
	}
	
	public void setButtonGroup(String bg) {
		option = bg;
	}
	
	public boolean getSetsResult() {
		return setsResult;
	}
	
	public void setSetsResult(boolean setsResult) {
		this.setsResult = setsResult;
	}
}
