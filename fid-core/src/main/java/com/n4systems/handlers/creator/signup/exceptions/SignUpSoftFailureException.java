package com.n4systems.handlers.creator.signup.exceptions;

import com.n4systems.exceptions.ProcessFailureException;

public class SignUpSoftFailureException extends ProcessFailureException {

	private static final long serialVersionUID = 1L;

	public SignUpSoftFailureException() {
		super();
	}

	public SignUpSoftFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignUpSoftFailureException(String message) {
		super(message);
	}

	public SignUpSoftFailureException(Throwable cause) {
		super(cause);
	}

	
}
