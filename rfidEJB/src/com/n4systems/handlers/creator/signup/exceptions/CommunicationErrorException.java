package com.n4systems.handlers.creator.signup.exceptions;


public class CommunicationErrorException extends SignUpSoftFailureException {

	private static final long serialVersionUID = 1L;

	public CommunicationErrorException() {
	}

	public CommunicationErrorException(String message) {
		super(message);
	}

	public CommunicationErrorException(Throwable cause) {
		super(cause);
	}

	public CommunicationErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
