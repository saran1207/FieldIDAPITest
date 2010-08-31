package com.n4systems.exceptions;

public class FileImportException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Long lineNumber = 0L;
	
	public FileImportException() {}

	public FileImportException(String message) {
		super(message);
	}
	
	public FileImportException(String message, Long lineNumber) {
		super(message);
		this.lineNumber = lineNumber;
	}
	
	public FileImportException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FileImportException(String message, Throwable cause, Long lineNumber) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}

	public Long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public String getMessage() {
		return "Line [" + lineNumber + "]: " + super.getMessage();
	}
	
	
}
