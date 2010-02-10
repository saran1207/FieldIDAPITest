package com.n4systems.exporting;

@SuppressWarnings("serial")
public class ExportException extends Exception {

	public ExportException() {
		super();
	}

	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExportException(String message) {
		super(message);
	}

	public ExportException(Throwable cause) {
		super(cause);
	}

}
