package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class EventType {
	String name;
	String group;
	boolean printable;
	boolean masterInspection;
	boolean assignedToAvailable;
	List<String> supportedProofTestTypes = new ArrayList<String>();
	List<String> inspectionAttributes = new ArrayList<String>();
	EventForm eventForm = null;
	
	public EventType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	public boolean isMasterInspection() {
		return masterInspection;
	}

	public void setMasterInspection(boolean masterInspection) {
		this.masterInspection = masterInspection;
	}

	public boolean isAssignedToAvailable() {
		return assignedToAvailable;
	}

	public void setAssignedToAvailable(boolean assignedToAvailable) {
		this.assignedToAvailable = assignedToAvailable;
	}

	public List<String> getSupportedProofTestTypes() {
		return supportedProofTestTypes;
	}

	public void setSupportedProofTestTypes(List<String> supportedProofTestTypes) {
		this.supportedProofTestTypes = supportedProofTestTypes;
	}

	public List<String> getInspectionAttributes() {
		return inspectionAttributes;
	}

	public void setInspectionAttributes(List<String> inspectionAttributes) {
		this.inspectionAttributes = inspectionAttributes;
	}

	public EventForm getInspectionForm() {
		return eventForm;
	}

	public void setInspectionForm(EventForm eventForm) {
		this.eventForm = eventForm;
	}
}
