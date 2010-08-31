package com.n4systems.security;

public class InvalidPermission extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidPermission() {
		super();
	}

	public InvalidPermission(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPermission(String message) {
		super(message);
	}

	public InvalidPermission(Throwable cause) {
		super(cause);
	}

}
