package com.n4systems.exceptions;

public class UncheckedLoadingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UncheckedLoadingException() {}

	public UncheckedLoadingException(String message) {
		super(message);
	}

	public UncheckedLoadingException(Throwable cause) {
		super(cause);
	}

	public UncheckedLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
