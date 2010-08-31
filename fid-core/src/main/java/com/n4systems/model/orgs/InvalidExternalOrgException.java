package com.n4systems.model.orgs;

public class InvalidExternalOrgException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidExternalOrgException() {
	}

	public InvalidExternalOrgException(String message) {
		super(message);
	}

	public InvalidExternalOrgException(Throwable cause) {
		super(cause);
	}

	public InvalidExternalOrgException(String message, Throwable cause) {
		super(message, cause);
	}

}
