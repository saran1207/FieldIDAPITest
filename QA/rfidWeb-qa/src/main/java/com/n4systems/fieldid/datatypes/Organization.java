package com.n4systems.fieldid.datatypes;

public class Organization {

	// Details
	String name = null;
	String nameOnCert = null;
	String countryTimezone = null;
	String timeZone = null;
	String webSiteAddress = null;	// only on primary organization
	
	// Address
	String streetAddress = null;
	String city = null;
	String state = null;
	String country = null;
	String zip = null;
	String phoneNumber = null;
	String fax = null;
	
	// Certificate Image
	String certImage = null;

	public Organization(String name) {
		this.name = name;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setNameOnCert(String s) {
		this.nameOnCert = s;
	}
	
	public void setCountryTimeZone(String s) {
		this.countryTimezone = s;
	}
	
	public void setTimeZone(String s) {
		this.timeZone = s;
	}
	
	public void setWebSiteAddress(String s) {
		this.webSiteAddress = s;
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
	
	public void setCountry(String s) {
		this.country = s;
	}
	
	public void setZip(String s) {
		this.zip = s;
	}
	
	public void setPhoneNumber(String s) {
		this.phoneNumber = s;
	}
	
	public void setFax(String s) {
		this.fax = s;
	}
	
	public void setCertImage(String s) {
		this.certImage = s;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameOnCert() {
		return this.nameOnCert;
	}
	
	public String getCountryTimeZone() {
		return this.countryTimezone;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public String getWebSiteAddress() {
		return this.webSiteAddress;
	}
	
	public String getStreetAddress() {
		return this.streetAddress;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public String getFax() {
		return this.fax;
	}
	
	public String getCertImage() {
		return this.certImage;
	}
}
