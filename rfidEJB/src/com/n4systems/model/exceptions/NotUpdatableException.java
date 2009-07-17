package com.n4systems.model.exceptions;

public class NotUpdatableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotUpdatableException() {
	}

	public NotUpdatableException(String arg0) {
		super(arg0);
	}

	public NotUpdatableException(Throwable arg0) {
		super(arg0);
	}

	public NotUpdatableException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
