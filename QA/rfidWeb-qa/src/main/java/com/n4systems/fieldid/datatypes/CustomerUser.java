package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.List;

public class CustomerUser {

	String userID = null;
	String email = null;
	String firstName = null;
	String lastName = null;
	String position = null;
	String initials = null;
	String securityRFIDNumber = null;
	String timeZone = null;
	String customer = null;
	String division = null;
	String password = null;
	List<String> permissions = new ArrayList<String>();	// "Create Inspections" & "Edit Inspections"
	// List of the possible permissions for an end user
	public final static String create = "Create Inspections";
	public final static String edit = "Edit Inspections";

	public CustomerUser(String userID, String email, String firstName, String lastName, String password) {
		this.userID = userID;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public void setSecurityRFIDNumber(String securityRFIDNumber) {
		this.securityRFIDNumber = securityRFIDNumber;
	}
	
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public void setCustomer(String customer) {
		this .customer = customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	// CustomerUser.create and CustomerUser.edit
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(String permission) {
		permissions.add(permission);
	}

	public String getUserID() {
		return userID;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getPosition() {
		return position;
	}

	public String getInitials() {
		return initials;
	}
	
	public String getSecurityRFIDNumber() {
		return securityRFIDNumber;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getDivision() {
		return division;
	}
	
	public String getPassword() {
		return password;
	}

	public List<String> getPermissions() {
		return permissions;
	}
	
	public String getPermission(int index) {
		return permissions.get(index);
	}
}
