package com.n4systems.fieldid.actions.helpers;

public class MissingEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingEntityException() {
		super();
	}

	public MissingEntityException(String message, Throwable cause) {
		super( message, cause );
	}

	public MissingEntityException(String message) {
		super( message );
	}

	public MissingEntityException(Throwable cause) {
		super( cause );
	}

}
