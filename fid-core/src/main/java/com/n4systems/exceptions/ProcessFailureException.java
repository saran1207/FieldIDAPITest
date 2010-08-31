package com.n4systems.exceptions;

public class ProcessFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProcessFailureException() {
	}

	public ProcessFailureException(String message) {
		super(message);
	}

	public ProcessFailureException(Throwable cause) {
		super(cause);
	}
	
	public ProcessFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
