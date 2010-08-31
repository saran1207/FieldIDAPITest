package com.n4systems.exporting.io;

@SuppressWarnings("serial")
public class EmptyDocumentException extends RuntimeException {

	public EmptyDocumentException() {
	}

	public EmptyDocumentException(String message) {
		super(message);
	}

	public EmptyDocumentException(Throwable cause) {
		super(cause);
	}

	public EmptyDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

}
