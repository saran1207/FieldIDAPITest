package com.n4systems.exceptions;

public class NonUniqueProductException extends Exception {

	private static final long serialVersionUID = 1L;

	public NonUniqueProductException() {
	}

	public NonUniqueProductException(String arg0) {
		super(arg0);
	}

	public NonUniqueProductException(Throwable arg0) {
		super(arg0);
	}

	public NonUniqueProductException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
