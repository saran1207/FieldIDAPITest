package com.n4systems.exceptions;

public class InvalidArgumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidArgumentException() {
	}

	public InvalidArgumentException(String arg0) {
		super(arg0);
	}

	public InvalidArgumentException(Throwable arg0) {
		super(arg0);
	}

	public InvalidArgumentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
