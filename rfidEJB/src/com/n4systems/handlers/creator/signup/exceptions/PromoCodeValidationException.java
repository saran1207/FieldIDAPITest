package com.n4systems.handlers.creator.signup.exceptions;


public class PromoCodeValidationException extends SignUpSoftFailureException {

	private static final long serialVersionUID = 1L;

	public PromoCodeValidationException() {
	}

	public PromoCodeValidationException(String message) {
		super(message);
	}

	public PromoCodeValidationException(Throwable cause) {
		super(cause);
	}

	public PromoCodeValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
