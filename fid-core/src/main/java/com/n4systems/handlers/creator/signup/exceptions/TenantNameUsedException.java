package com.n4systems.handlers.creator.signup.exceptions;


public class TenantNameUsedException extends SignUpSoftFailureException {

	private static final long serialVersionUID = 1L;

	public TenantNameUsedException() {
	}

	public TenantNameUsedException(String message) {
		super(message);
	}

	public TenantNameUsedException(Throwable cause) {
		super(cause);
	}

	public TenantNameUsedException(String message, Throwable cause) {
		super(message, cause);
	}

}
