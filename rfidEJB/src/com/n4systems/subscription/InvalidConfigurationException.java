package com.n4systems.subscription;

public class InvalidConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidConfigurationException() {
	}

	public InvalidConfigurationException(String arg0) {
		super(arg0);
	}

	public InvalidConfigurationException(Throwable arg0) {
		super(arg0);
	}

	public InvalidConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
