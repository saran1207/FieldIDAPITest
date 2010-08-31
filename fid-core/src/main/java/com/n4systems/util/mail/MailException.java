package com.n4systems.util.mail;

@SuppressWarnings("serial")
public class MailException extends RuntimeException {

	public MailException() {
	}

	public MailException(String message) {
		super(message);
	}

	public MailException(Throwable cause) {
		super(cause);
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

}
