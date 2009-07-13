package com.n4systems.exceptions;

public class UpdateFailureException extends Exception {
	private static final long serialVersionUID = 1L;

	public UpdateFailureException() {
		super();
	}

	public UpdateFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateFailureException(String message) {
		super(message);
	}

	public UpdateFailureException(Throwable cause) {
		super(cause);
	}
	
}
