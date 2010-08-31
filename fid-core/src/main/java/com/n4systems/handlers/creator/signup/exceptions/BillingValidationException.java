package com.n4systems.handlers.creator.signup.exceptions;


public class BillingValidationException extends SignUpSoftFailureException {
	private static final long serialVersionUID = 1L;

	public BillingValidationException() {
	}

	public BillingValidationException(String message) {
		super(message);
	}

	public BillingValidationException(Throwable cause) {
		super(cause);
	}

	public BillingValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
