package com.n4systems.fieldid.selenium.datatypes;

import java.util.ArrayList;
import java.util.List;

public class EmployeeUser extends SystemUser {
	
	private List<String> permissions = new ArrayList<String>();

	// List of the possible permissions for employee users
	public final static String tag = "Identify Assets";
	public final static String sysconfig = "Manage System Configuration";
	public final static String sysusers = "Manage System Users";
	public final static String endusers = "Manage Customers";
	public final static String create = "Create Events";
	public final static String edit = "Edit Events";
	public final static String jobs = "Manage Jobs";
	public final static String safety = "Manage Safety Network";
	public final static String store = "Access Web Store";

	public EmployeeUser(String userid, String email, String password, String verifyPassword, Owner owner, String firstName, String lastName) {
		super(userid, email, password, verifyPassword, owner, firstName, lastName);
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	public String getPermission(int index) {
		return permissions.get(index);
	}
	
	public void addPermission(String permission) {
		permissions.add(permission);
	}
	
	public void removePermission(String permission) {
		permissions.remove(permission);
	}

	public void removePermission(int index) {
		permissions.remove(index);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(super.toString());
		for(String permission : permissions) {
			s.append("\n      Permission Set: " + permission);
		}
		return s.toString();
	}
}
