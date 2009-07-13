package com.n4systems.exceptions;

public class OrderProcessingException extends Exception {
	private static final long serialVersionUID = 1L;

	public OrderProcessingException() {}

	public OrderProcessingException(String message) {
		super(message);
	}

	public OrderProcessingException(Throwable cause) {
		super(cause);
	}

	public OrderProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
