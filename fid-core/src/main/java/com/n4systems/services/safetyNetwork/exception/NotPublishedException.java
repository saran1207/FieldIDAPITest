package com.n4systems.services.safetyNetwork.exception;

public class NotPublishedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotPublishedException() {
	}

	public NotPublishedException(String message) {
		super(message);
	}

	public NotPublishedException(Throwable cause) {
		super(cause);
	}

	public NotPublishedException(String message, Throwable cause) {
		super(message, cause);
	}

}
