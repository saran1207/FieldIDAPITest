package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionType {
	String name;
	String group;
	boolean printable;
	boolean masterInspection;
	boolean assignedToAvailable;
	List<String> supportedProofTestTypes = new ArrayList<String>();
	List<String> inspectionAttributes = new ArrayList<String>();
	InspectionForm inspectionForm = null;
	
	public InspectionType(String name) {
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

	public InspectionForm getInspectionForm() {
		return inspectionForm;
	}

	public void setInspectionForm(InspectionForm inspectionForm) {
		this.inspectionForm = inspectionForm;
	}
}
