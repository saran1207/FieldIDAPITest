package com.n4systems.fieldid.datatypes;

public class MassUpdateScheduleForm {

	String nextInspectionDate = null;
	
	public MassUpdateScheduleForm() {
		super();
	}
	
	public MassUpdateScheduleForm(String nextInspectionDate) {
		this.nextInspectionDate = nextInspectionDate;
	}
	
	public void setNextInspectionDate(String s) {
		nextInspectionDate = s;
	}
	
	public String getNextInspectionDate() {
		return nextInspectionDate;
	}
}
