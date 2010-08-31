package com.n4systems.exceptions;



public class EmptyReportException extends ReportException {
	private static final long serialVersionUID = 1L;

	public EmptyReportException() {
		super();
	}

	public EmptyReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyReportException(String message) {
		super(message);
	}

	public EmptyReportException(Throwable cause) {
		super(cause);
	}
}
