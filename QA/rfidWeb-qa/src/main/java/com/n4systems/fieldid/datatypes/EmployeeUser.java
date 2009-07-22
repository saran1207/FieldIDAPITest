package com.n4systems.fieldid.datatypes;

public class EmployeeUser extends SystemUser {

	String orgUnit = null;
	// List of the possible permissions for an employee user
	public final static String tag = "Tag Products";
	public final static String sysconfig = "Manage System Configuration";
	public final static String sysusers = "Manage System Users";
	public final static String endusers = "Manage End Users";
	public final static String create = "Create Inspections";
	public final static String edit = "Edit Inspections";
	public final static String jobs = "Manage Jobs";
	
	public EmployeeUser(String userID, String email, String firstName, String lastName, String password) {
		super(userID, email, firstName, lastName, password);
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}
	
	public String getOrgUnit() {
		return this.orgUnit;
	}
}
