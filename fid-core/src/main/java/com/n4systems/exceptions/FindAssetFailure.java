package com.n4systems.exceptions;

public class FindAssetFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public FindAssetFailure() {
		super();
	}

	public FindAssetFailure(String message, Throwable cause) {
		super( message, cause );
	}

	public FindAssetFailure(String message) {
		super( message );
	}

	public FindAssetFailure(Throwable cause) {
		super( cause );
	}

}
