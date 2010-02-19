package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionFormCriteria {
	String criteriaLabel;
	String buttonGroup;
	boolean setsResult;
	List<InspectionFormObservations> observations = new ArrayList<InspectionFormObservations>();
	
	public InspectionFormCriteria(String criteriaLabel, String buttonGroup) {
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

	public List<InspectionFormObservations> getObservations() {
		return observations;
	}
	
	public InspectionFormObservations getObservation(int index) {
		if(observations.size() < index) {
			return null;
		}
		return observations.get(index);
	}

	public void setObservations(List<InspectionFormObservations> observations) {
		this.observations = observations;
	}

	public void addObservation(int index, InspectionFormObservations element) {
		observations.add(index, element);
	}
}
