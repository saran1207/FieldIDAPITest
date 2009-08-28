package com.n4systems.subscription;

public class BillingInfoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private final BillingInfoField problemField;
	
	public BillingInfoException(BillingInfoField problemField) {
		this.problemField = problemField;
	}
	
	public BillingInfoException() {
		this(BillingInfoField.UNKOWN);
	}

	public BillingInfoField getProblemField() {
		return problemField;
	}
}
