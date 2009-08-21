package com.n4systems.netsuite.model;

public class CreditCard {

	private String number;
	private CreditCardType type;
	private String expiry; // ex. 1/08 for January 2008 expiry
	private String name;

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public CreditCardType getType() {
		return type;
	}
	public void setType(CreditCardType type) {
		this.type = type;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
