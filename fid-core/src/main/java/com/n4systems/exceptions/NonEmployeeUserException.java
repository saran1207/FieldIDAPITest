package com.n4systems.exceptions;

public class NonEmployeeUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public NonEmployeeUserException() {
	}

	public NonEmployeeUserException(String arg0) {
		super(arg0);
	}

	public NonEmployeeUserException(Throwable arg0) {
		super(arg0);
	}

	public NonEmployeeUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
