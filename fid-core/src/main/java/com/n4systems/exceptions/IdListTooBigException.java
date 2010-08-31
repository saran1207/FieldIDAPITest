package com.n4systems.exceptions;

public class IdListTooBigException extends RuntimeException {

	public IdListTooBigException() {
	}

	public IdListTooBigException(String message) {
		super(message);
	}

	public IdListTooBigException(Throwable cause) {
		super(cause);
	}

	public IdListTooBigException(String message, Throwable cause) {
		super(message, cause);
	}

}
