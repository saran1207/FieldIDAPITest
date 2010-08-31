package com.n4systems.util.reflection;

public class ReflectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ReflectionException() {}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
