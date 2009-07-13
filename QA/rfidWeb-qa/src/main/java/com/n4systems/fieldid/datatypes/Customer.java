package com.n4systems.fieldid.datatypes;

public class Customer {
	String customerID = null;
	String customerName = null;
	String contactName = null;
	String contactEmail = null;
	String streetAddress = null;
	String city = null;
	String state = null;
	String zip = null;
	String country = null;
	String phone1 = null;
	String phone2 = null;
	String fax = null;
	
	public Customer(String customerID, String customerName) {
		this.customerID = customerID;
		this.customerName = customerName;
	}
	
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public String getContactName() {
		return contactName;
	}
	
	public String getContactEmail() {
		return contactEmail;
	}

	public String getStreetAddress() {
		return streetAddress;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getPhone1() {
		return phone1;
	}
	
	public String getPhone2() {
		return phone2;
	}
	
	public String getFax() {
		return fax;
	}
}
