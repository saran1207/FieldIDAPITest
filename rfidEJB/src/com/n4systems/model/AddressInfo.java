package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name = "addressinfo")
public class AddressInfo extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	private String streetAddress = "";
	private String city;
	private String state;
	private String country;
	private String zip;
	private String phone1;
	private String phone2;
	private String fax1;
	
	
	
	public AddressInfo() {
		super();
	}
	
	public AddressInfo(AddressInfo addressInfo) {
		super();
		streetAddress = addressInfo.streetAddress;
		city = addressInfo.city;
		state = addressInfo.state;
		country = addressInfo.country;
		zip = addressInfo.zip;
		phone1 = addressInfo.phone1;
		phone2 = addressInfo.phone2;
		fax1 = addressInfo.fax1;
		
		
	}

	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String address) {
		this.streetAddress = (address != null) ? address : "";
		this.streetAddress = streetAddress.trim();
	}
	
	public void setStreetAddress(String addressLine1, String addressLine2) {
		setStreetAddress(((addressLine1 != null) ? addressLine1 : "") + " " + ((addressLine2 != null) ? addressLine2 : ""));
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String postalCode) {
		this.zip = postalCode;
	}
	
	public String getPhone1() {
		return phone1;
	}
	
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	
	public String getPhone2() {
		return phone2;
	}
	
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	
	public String getFax1() {
		return fax1;
	}
	
	public void setFax1(String faxNumber) {
		this.fax1 = faxNumber;
	}
	
	public String getDisplay() {
		String display = streetAddress + "\n" +
			city + ", " + state + "\n" +
			country + "\n" +
			zip;
		
		if(phone1 != null) {
			display += "\nPhone 1: " + phone1;
		}
		
		if(phone2 != null) {
			display += "\nPhone 2: " + phone2;
		}

		if(fax1 != null) {
			display += "\nFax: " + fax1;
		}
		
		return display;
	}
	
	public void copyFieldsTo(AddressInfo other) {
		other.streetAddress = streetAddress;
		other.city = city;
		other.state = state;
		other.country = country;
		other.zip = zip;
		other.phone1 = phone1;
		other.phone2 = phone2;
		other.fax1 = fax1;
	}

	@Override
	public String toString() {
		return "AddressInfo [city=" + city + ", country=" + country + ", fax1=" + fax1 + ", phone1=" + phone1 + ", phone2=" + phone2 + ", state=" + state + ", streetAddress=" + streetAddress
				+ ", zip=" + zip + "]";
	}
	
	
	
	
	
	
}
