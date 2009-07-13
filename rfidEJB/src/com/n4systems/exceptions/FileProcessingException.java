package com.n4systems.exceptions;

public class FileProcessingException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileProcessingException() {
		super();
	}

	public FileProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileProcessingException(String message) {
		super(message);
	}

	public FileProcessingException(Throwable cause) {
		super(cause);
	}

}
