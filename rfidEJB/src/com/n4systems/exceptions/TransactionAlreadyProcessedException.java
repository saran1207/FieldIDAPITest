package com.n4systems.exceptions;

public class TransactionAlreadyProcessedException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransactionAlreadyProcessedException() {
		super();
	}

	public TransactionAlreadyProcessedException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

	public TransactionAlreadyProcessedException(String arg0) {
		super( arg0 );
	}

	public TransactionAlreadyProcessedException(Throwable arg0) {
		super( arg0 );
	}

}
