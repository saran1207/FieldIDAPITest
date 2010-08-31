package com.n4systems.exceptions;

public class AssetAlreadyAttachedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AssetAlreadyAttachedException() {
	}

	public AssetAlreadyAttachedException( String arg0 ) {
		super( arg0 );
	}

	public AssetAlreadyAttachedException( Throwable arg0 ) {
		super( arg0 );
	}

	public AssetAlreadyAttachedException( String arg0, Throwable arg1 ) {
		super( arg0, arg1 );
	}

}
