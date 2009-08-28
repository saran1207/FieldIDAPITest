package com.n4systems.subscription;

public class CreditCard {

	private String number;
	private CreditCardType type;
	private int expiryMonth;
	private int expiryYear;
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
		return expiryMonth + "/" + expiryYear;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(int expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public int getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(int expiryYear) {
		this.expiryYear = expiryYear;
	}
}
