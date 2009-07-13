package com.n4systems.exceptions;

public class SubProductUniquenessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubProductUniquenessException() {
		super();
	}

	public SubProductUniquenessException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

	public SubProductUniquenessException(String arg0) {
		super( arg0 );
	}

	public SubProductUniquenessException(Throwable arg0) {
		super( arg0 );
	}

}
