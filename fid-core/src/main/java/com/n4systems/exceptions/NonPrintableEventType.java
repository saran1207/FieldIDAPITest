package com.n4systems.exceptions;

public class NonPrintableEventType extends ReportException {
	private static final long serialVersionUID = 1L;

	public NonPrintableEventType() {
		super( "Inspection doc is not printable" );
	}

	public NonPrintableEventType(String arg0) {
		super(arg0);
	}

	public NonPrintableEventType(Throwable arg0) {
		super("Inspection doc is not printable", arg0);
	}

	public NonPrintableEventType(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
