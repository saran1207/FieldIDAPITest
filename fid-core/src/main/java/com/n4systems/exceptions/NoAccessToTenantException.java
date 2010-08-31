package com.n4systems.exceptions;

public class NoAccessToTenantException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoAccessToTenantException() {
	}

	public NoAccessToTenantException(String arg0) {
		super(arg0);
	}

	public NoAccessToTenantException(Throwable arg0) {
		super(arg0);
	}

	public NoAccessToTenantException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
