package com.n4systems.exceptions;

public class NonPrintableManufacturerCert extends ReportException {

	private static final long serialVersionUID = 1L;

	public NonPrintableManufacturerCert() {
		this( "product manufacturer cert  is not printable" );
	}

	public NonPrintableManufacturerCert(String message) {
		super( message );
	}

	public NonPrintableManufacturerCert(Throwable cause) {
		this( "product manufacturer cert  is not printable", cause );
	}

	public NonPrintableManufacturerCert(String message, Throwable cause) {
		super( message, cause );
	}

}
