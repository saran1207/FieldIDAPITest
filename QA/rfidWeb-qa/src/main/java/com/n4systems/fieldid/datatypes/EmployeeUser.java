package com.n4systems.fieldid.datatypes;

import java.util.Iterator;

public class EmployeeUser extends SystemUser {

	String orgUnit = null;
	// List of the possible permissions for an employee user
	public final static String tag = "Tag Products";
	public final static String sysconfig = "Manage System Configuration";
	public final static String sysusers = "Manage System Users";
	public final static String endusers = "Manage End Users";
	public final static String jobs = "Manage Jobs";
	public final static String safety = "Manage Safety Network";
	public final static String store = "Access Web Store";
	
	public EmployeeUser(String userID, String email, String firstName, String lastName, String password) {
		super(userID, email, firstName, lastName, password);
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}
	
	public String getOrgUnit() {
		return this.orgUnit;
	}
	public void addAllPermissions() {
		super.addAllPermissions();
		permissions.add(tag);
		permissions.add(sysconfig);
		permissions.add(sysusers);
		permissions.add(endusers);
		permissions.add(jobs);
		permissions.add(safety);
		permissions.add(store);
	}

	public String toString() {
		StringBuffer s = new StringBuffer(super.toString());
	
		Iterator<String> i = permissions.iterator();
		while(i.hasNext()) {
			String p = i.next();
			s.append(",");	s.append(p);
		}
		return s.toString();
	}
}
