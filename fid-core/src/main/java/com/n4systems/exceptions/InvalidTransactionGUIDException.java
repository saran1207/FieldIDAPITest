package com.n4systems.exceptions;

public class InvalidTransactionGUIDException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public InvalidTransactionGUIDException() {
	}

	public InvalidTransactionGUIDException(String arg0) {
		super( arg0 );
	}

	public InvalidTransactionGUIDException(Throwable arg0) {
		super( arg0 );
	}

	public InvalidTransactionGUIDException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

}
