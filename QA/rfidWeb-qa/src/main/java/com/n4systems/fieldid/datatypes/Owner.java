package com.n4systems.fieldid.datatypes;

public class Owner {
	String organization = null;
	String customer = null;
	String division = null;
	
	public Owner(String organization) {
		this.organization = organization;
	}

	public Owner(String organization, String customer) {
		this(organization);
		this.customer = customer;
	}

	public Owner(String organization, String customer, String division) {
		this(organization, customer);
		this.division = division;
	}
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getOrganization() {
		return this.organization;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public String getDivision() {
		return this.division;
	}
}
