package com.n4systems.subscription;

public class CommunicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CommunicationException() {
		super();
	}

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(Throwable cause) {
		super(cause);
	}


	
}
