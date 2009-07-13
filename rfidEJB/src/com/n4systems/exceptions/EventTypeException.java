package com.n4systems.exceptions;

public class EventTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public EventTypeException() {
		super();
	}

	public EventTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventTypeException(String message) {
		super(message);
	}

	public EventTypeException(Throwable cause) {
		super(cause);
	}

}
