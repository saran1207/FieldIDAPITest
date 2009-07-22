package com.n4systems.fieldid.datatypes;

public class CustomerUser extends SystemUser {

	String customer = null;
	String division = null;

	public CustomerUser(String userID, String email, String firstName, String lastName, String password) {
		super(userID, email, firstName, lastName, password);
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getDivision() {
		return division;
	}
}
