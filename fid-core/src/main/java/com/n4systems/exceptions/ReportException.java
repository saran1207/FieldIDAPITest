package com.n4systems.exceptions;


public class ReportException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ReportException() {
		super();
	}
	
	public ReportException(String msg) {
		super(msg);
	}
	
	public ReportException(String msg, Throwable arg0) {
		super(msg, arg0);
	}
	
	public ReportException(Throwable cause) {
		super(cause);
	}
}
