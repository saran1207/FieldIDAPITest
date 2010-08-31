package com.n4systems.exporting;

@SuppressWarnings("serial")
public class ImportException extends Exception {
	private int lineNumber = -1;
	
	public ImportException() {
		super();
	}

	public ImportException(String message, Throwable cause, int lineNumber) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}

	public ImportException(String message) {
		super(message);
	}

	public ImportException(Throwable cause) {
		super(cause);
	}

	public int getLineNumber() {
		return lineNumber;
	}

}
