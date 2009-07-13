package com.n4systems.exceptions;

public class TenantNotValidForActionException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public TenantNotValidForActionException() {
		super();
	}

	public TenantNotValidForActionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TenantNotValidForActionException(String arg0) {
		super(arg0);
	}

	public TenantNotValidForActionException(Throwable arg0) {
		super(arg0);
	}

}
