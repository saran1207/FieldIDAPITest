package com.n4systems.exceptions;

public class InvalidQueryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidQueryException() {
		super();
	}

	public InvalidQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidQueryException(String message) {
		super(message);
	}

	public InvalidQueryException(Throwable cause) {
		super(cause);
	}
	
}
