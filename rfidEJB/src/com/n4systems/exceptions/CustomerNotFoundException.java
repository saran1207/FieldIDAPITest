package com.n4systems.exceptions;

public class CustomerNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException() {}

	public CustomerNotFoundException(Long customerId) {
		super("Customer with id [" + customerId + "] does not exist");
	}
	
	public CustomerNotFoundException(String message) {
		super(message);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);
	}

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
