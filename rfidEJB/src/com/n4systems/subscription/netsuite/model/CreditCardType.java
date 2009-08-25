package com.n4systems.subscription.netsuite.model;

public enum CreditCardType {

	Visa("1"),
	MasterCard("2"),
	AMEX("3");
	
	private String code;
	
	private CreditCardType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
