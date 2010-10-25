package com.n4systems.exceptions;

public class UnknownSubAsset extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownSubAsset() {
	}

	public UnknownSubAsset(String arg0) {
		super( arg0 );
	}

	public UnknownSubAsset(Throwable arg0) {
		super( arg0 );
	}

	public UnknownSubAsset(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

}
