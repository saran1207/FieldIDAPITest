package com.n4systems.api.conversion;

public class ConversionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConversionException() {
		super();
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

}
