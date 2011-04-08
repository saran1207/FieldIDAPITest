package com.n4systems.exceptions;

import com.n4systems.exceptions.InvalidArgumentException;

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
