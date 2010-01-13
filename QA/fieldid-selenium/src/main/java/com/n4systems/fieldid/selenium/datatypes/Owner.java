package com.n4systems.fieldid.selenium.datatypes;

public class Owner {
	String organization;
	String customer;
	String division;
	
	public Owner(String organization, String customer, String division) {
		this.organization = organization;
		this.customer = customer;
		this.division = division;
	}
	
	public Owner(String organization, String customer) {
		this.organization = organization;
		this.customer = customer;
	}
	
	public Owner(String organization) {
		this.organization = organization;
	}
	
	public Owner() {
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
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
}
