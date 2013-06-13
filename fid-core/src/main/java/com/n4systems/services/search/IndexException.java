package com.n4systems.services.search;

public class IndexException extends RuntimeException {
	public IndexException() {
	}

	public IndexException(String message) {
		super(message);
	}

	public IndexException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndexException(Throwable cause) {
		super(cause);
	}
}
