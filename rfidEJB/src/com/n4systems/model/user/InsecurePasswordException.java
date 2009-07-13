package com.n4systems.model.user;

public class InsecurePasswordException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsecurePasswordException() {
	}

	public InsecurePasswordException(String message) {
		super(message);
	}

	public InsecurePasswordException(Throwable cause) {
		super(cause);
	}

	public InsecurePasswordException(String message, Throwable cause) {
		super(message, cause);
	}

}
