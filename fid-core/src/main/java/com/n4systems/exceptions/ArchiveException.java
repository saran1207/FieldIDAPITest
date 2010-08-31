package com.n4systems.exceptions;

public class ArchiveException extends Exception {

	private static final long serialVersionUID = 1L;

	public ArchiveException() {
	}

	public ArchiveException( String arg0 ) {
		super( arg0 );
	}

	public ArchiveException( Throwable arg0 ) {
		super( arg0 );
	}

	public ArchiveException( String arg0, Throwable arg1 ) {
		super( arg0, arg1 );
	}

}
