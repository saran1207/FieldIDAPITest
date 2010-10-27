package com.n4systems.exceptions;

public class NonUniqueAssetException extends Exception {

	private static final long serialVersionUID = 1L;

	public NonUniqueAssetException() {
	}

	public NonUniqueAssetException(String arg0) {
		super(arg0);
	}

	public NonUniqueAssetException(Throwable arg0) {
		super(arg0);
	}

	public NonUniqueAssetException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
