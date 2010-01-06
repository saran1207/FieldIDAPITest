package com.n4systems.webservice.server;

public class NoSerialNumbersException extends RuntimeException {

	public NoSerialNumbersException() {
		super();
	}

	public NoSerialNumbersException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSerialNumbersException(String message) {
		super(message);
	}

	public NoSerialNumbersException(Throwable cause) {
		super(cause);
	}

}
