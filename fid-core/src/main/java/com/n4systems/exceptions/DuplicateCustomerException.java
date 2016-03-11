package com.n4systems.exceptions;

public class DuplicateCustomerException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public DuplicateCustomerException() {
	}

	public DuplicateCustomerException(String message) {
		super(message);
	}

	public DuplicateCustomerException(Throwable cause) {
		super(cause);
	}

	public DuplicateCustomerException(String message, Throwable cause) {
		super(message, cause);
	}

}
