package com.n4systems.fieldid.datatypes;

public class CustomerDivision {

	String divisionID;
	String divisionName;
	String contactName;
	String contactEmail;
	String streetAddress;
	String city;
	String state;
	String zip;
	String country;
	String phone1;
	String phone2;
	String fax;
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append((divisionName == null) ? "" : divisionName); s.append(",");
		s.append((divisionID == null) ? "" : divisionID); s.append(",");
		s.append((contactName == null) ? "" : contactName); s.append(",");
		s.append((contactEmail == null) ? "" : contactEmail); s.append(",");
		s.append((streetAddress == null) ? "" : streetAddress); s.append(",");
		s.append((city == null) ? "" : city); s.append(",");
		s.append((state == null) ? "" : state); s.append(",");
		s.append((zip == null) ? "" : zip); s.append(",");
		s.append((country == null) ? "" : country); s.append(",");
		s.append((phone1 == null) ? "" : phone1); s.append(",");
		s.append((phone2 == null) ? "" : phone2); s.append(",");
		s.append((fax == null) ? "" : fax);
		
		return s.toString();
	}
	
	public CustomerDivision(String divisionID, String divisionName) {
		this.divisionID = divisionID;
		this.divisionName = divisionName;
	}

	public void setDivisionID(String s) {
		this.divisionID = s;
	}

	public void setDivisionName(String s) {
		this.divisionName = s;
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
	
	public void setZip(String s) {
		this.zip = s;
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

	public String getDivisionID() {
		return divisionID;
	}

	public String getDivisionName() {
		return divisionName;
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
