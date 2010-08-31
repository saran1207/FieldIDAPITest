package com.n4systems.exceptions;

public class FindProductFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public FindProductFailure() {
		super();
	}

	public FindProductFailure(String message, Throwable cause) {
		super( message, cause );
	}

	public FindProductFailure(String message) {
		super( message );
	}

	public FindProductFailure(Throwable cause) {
		super( cause );
	}

}
