package com.n4systems.fieldid.selenium.datatypes;

public class Organization {
	String name;
	String nameOnCert;
	String country;
	String timeZone;
	String companyStreetAddress;
	String companyCity;
	String companyState;
	String companyCountry;
	String companyZipCode;
	String companyPhoneNumber;
	String companyFaxNumber;
	String certificateImageFileName;

	public Organization(String name) {
		this.name = name;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setNameOnCert(String s) {
		this.nameOnCert = s;
	}
	
	public void setCountry(String s) {
		this.country = s;
	}
	
	public void setTimeZone(String s) {
		this.timeZone = s;
	}
	
	public void setCompanyStreetAddress(String s) {
		this.companyStreetAddress = s;
	}
	
	public void setCompanyCity(String s) {
		this.companyCity = s;
	}
	
	public void setCompanyState(String s) {
		this.companyState = s;
	}
	
	public void setCompanyCountry(String s) {
		this.companyCountry = s;
	}
	
	public void setCompanyZipCode(String s) {
		this.companyZipCode = s;
	}
	
	public void setCompanyPhoneNumber(String s) {
		this.companyPhoneNumber = s;
	}
	
	public void setCompanyFaxNumber(String s) {
		this.companyFaxNumber = s;
	}
	
	public void setcertificateImageFileName(String s) {
		this.certificateImageFileName = s;
	}

	public String getName() {
		return name;
	}
	
	public String getNameOnCert() {
		return nameOnCert;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public String getCompanyStreetAddress() {
		return companyStreetAddress;
	}
	
	public String getCompanyCity() {
		return companyCity;
	}
	
	public String getCompanyState() {
		return companyState;
	}
	
	public String getCompanyCountry() {
		return companyCountry;
	}
	
	public String getCompanyZipCode() {
		return companyZipCode;
	}
	
	public String getCompanyPhoneNumber() {
		return companyPhoneNumber;
	}
	
	public String getCompanyFaxNumber() {
		return companyFaxNumber;
	}
	
	public String getcertificateImageFileName() {
		return certificateImageFileName;
	}
}
