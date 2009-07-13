package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.List;

public class InspectionType {

	String name = null;
	String group = null;
	boolean printable = false;
	boolean masterInspection = false;
	List<String> proofTestTypes = new ArrayList<String>();
	public final static String robert = "Robert Log file";
	public final static String naexcel = "NA Excel file";
	public final static String chant = "Chant Log file";
	public final static String wirop = "Wirop Log file";
	public final static String other = "Other";
	List<String> inspectionAttributes = new ArrayList<String>();
	
	public InspectionType(String name) {
		this.name = name;
	}
	
	public InspectionType(String name, String group) {
		this(name);
		this.group = group;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	
	public boolean getPrintable() {
		return printable;
	}
	
	public void setMasterInspection(boolean masterInspection) {
		this.masterInspection = masterInspection;
	}
	
	public boolean getMasterInspection() {
		return masterInspection;
	}
	
	public void setProofTestTypes(List<String> proofTestTypes) {
		this.proofTestTypes = proofTestTypes;
	}
	
	public void addProofTestType(String proofTestType) {
		proofTestTypes.add(proofTestType);
	}
	
	public void deleteProofTestType(String proofTestType) {
		proofTestTypes.remove(proofTestType);
	}
	
	public List<String> getProofTestTypes() {
		return proofTestTypes;
	}

	public void setInspectionAttributes(List<String> inspectionAttributes) {
		this.inspectionAttributes = inspectionAttributes;
	}
	
	public void addInspectionAttributes(String inspectionAttribute) {
		inspectionAttributes.add(inspectionAttribute);
	}
	
	public void deleteInspectionAttributes(String inspectionAttribute) {
		inspectionAttributes.remove(inspectionAttribute);
	}
	
	public List<String> getInspectionAttributes() {
		return inspectionAttributes;
	}
}
