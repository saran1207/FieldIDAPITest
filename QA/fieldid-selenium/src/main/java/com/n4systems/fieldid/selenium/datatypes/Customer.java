package com.n4systems.fieldid.selenium.datatypes;

public class Customer {
	String customerID;
	String customerName;
	String organizationalUnit;
	String contactName;
	String contactEmail;
	String streetAddress;
	String city;
	String state;
	String zipCode;
	String country;
	String phone1;
	String phone2;
	String fax;

	public Customer(String customerID, String customerName) {
		this.customerID = customerID;
		this.customerName = customerName;
	}
	
	public void setCustomerID(String s) {
		this.customerID = s;
	}
	
	public void setCustomerName(String s) {
		this.customerName = s;
	}
	
	public void setOrganizationalUnit(String s) {
		this.organizationalUnit = s;
	}
	
	public void setContactName(String s) {
		this.contactName = s;
	}
	
	public void setContactEmail(String s) {
		this.contactEmail = s;
	}
	
	public void setStreetAddress(String s) {
		this.streetAddress = s;
	}
	
	public void setCity(String s) {
		this.city = s;
	}
	
	public void setState(String s) {
		this.state = s;
	}
	
	public void setZipCode(String s) {
		this.zipCode = s;
	}
	
	public void setCountry(String s) {
		this.country = s;
	}
	
	public void setPhone1(String s) {
		this.phone1 = s;
	}

	public void setPhone2(String s) {
		this.phone2 = s;
	}

	public void setFax(String s) {
		this.fax = s;
	}

	public String getCustomerID() {
		return customerID;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public String getOrganizationalUnit() {
		return organizationalUnit;
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
	
	public String getZipCode() {
		return zipCode;
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
