package com.n4systems.exceptions;

public class ProcessingProofTestException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public ProcessingProofTestException() {
	}

	public ProcessingProofTestException(String message) {
		super( message );
	}

	public ProcessingProofTestException(Throwable cause) {
		super( cause );
		
	}

	public ProcessingProofTestException(String message, Throwable cause) {
		super( message, cause );
	}

}
