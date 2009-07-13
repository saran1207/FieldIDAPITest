package com.n4systems.exceptions;

public class EmployeeAlreadyAttachedException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmployeeAlreadyAttachedException() {
	}

	public EmployeeAlreadyAttachedException(String arg0) {
		super(arg0);
	}

	public EmployeeAlreadyAttachedException(Throwable arg0) {
		super(arg0);
	}

	public EmployeeAlreadyAttachedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
