package com.n4systems.fieldid.permissions;

public class NoValidTenantSelectedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoValidTenantSelectedException() {
	}

	public NoValidTenantSelectedException(String arg0) {
		super(arg0);
	}

	public NoValidTenantSelectedException(Throwable arg0) {
		super(arg0);
	}

	public NoValidTenantSelectedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
