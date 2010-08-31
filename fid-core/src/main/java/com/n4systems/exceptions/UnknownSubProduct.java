package com.n4systems.exceptions;

public class UnknownSubProduct extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnknownSubProduct() {
	}

	public UnknownSubProduct(String arg0) {
		super( arg0 );
	}

	public UnknownSubProduct(Throwable arg0) {
		super( arg0 );
	}

	public UnknownSubProduct(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

}
