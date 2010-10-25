package com.n4systems.exceptions;

public class SubAssetUniquenessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubAssetUniquenessException() {
		super();
	}

	public SubAssetUniquenessException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

	public SubAssetUniquenessException(String arg0) {
		super( arg0 );
	}

	public SubAssetUniquenessException(Throwable arg0) {
		super( arg0 );
	}

}
