package com.n4systems.exceptions;

public class NonPrintableEventType extends ReportException {
	private static final long serialVersionUID = 1L;

	public NonPrintableEventType() {
		super( "Event doc is not printable" );
	}

	public NonPrintableEventType(String message) {
		super(message);
	}

	public NonPrintableEventType(Throwable cause) {
		super("Event doc is not printable", cause);
	}

	public NonPrintableEventType(String message, Throwable cause) {
		super(message, cause);
	}

}
