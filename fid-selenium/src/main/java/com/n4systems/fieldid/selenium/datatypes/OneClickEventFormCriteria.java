package com.n4systems.fieldid.selenium.datatypes;


public class OneClickEventFormCriteria {
	String criteriaLabel;
	String buttonGroup;
	boolean setsResult;
	EventFormObservations observations;
	
	public OneClickEventFormCriteria(String criteriaLabel, String buttonGroup) {
		this.criteriaLabel = criteriaLabel;
		this.buttonGroup = buttonGroup;
	}

	public String getCriteriaLabel() {
		return criteriaLabel;
	}

	public void setCriteriaLabel(String criteriaLabel) {
		this.criteriaLabel = criteriaLabel;
	}

	public String getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(String buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	public boolean isSetsResult() {
		return setsResult;
	}

	public void setSetsResult(boolean setsResult) {
		this.setsResult = setsResult;
	}

	public EventFormObservations getObservations() {
		return observations;
	}
	
	public void setObservations(EventFormObservations observations) {
		this.observations = observations;
	}
}
