package com.n4systems.fieldid.selenium.datatypes;

public class Owner {
	public static Owner someOrg() {
		return new Owner();
	}
	
	public final String organization;
	public final String customer;
	public final String division;
	
	public Owner(String organization, String customer, String division) {
		this.organization = organization;
		this.customer = customer;
		this.division = division;
	}
	
	public Owner(String organization, String customer) {
		this(organization, customer, null);
	}
	
	public Owner(String organization) {
		this(organization, null, null);
	}
	
	private Owner() {
		this(null, null, null);
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("\n        Organization: " + (organization != null ? organization : "{default}"));
		s.append("\n            Customer: " + (customer != null ? customer : "{default}"));
		s.append("\n            Division: " + (division != null ? division : "{default}"));
		return s.toString();
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
	
	
	public boolean specifiesOrg() {
		return organization != null;
	}
	
}
