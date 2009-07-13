package com.n4systems.exceptions;

public class ImageAttachmentException extends Exception {

	private static final long serialVersionUID = 1L;

	public ImageAttachmentException() {
	}

	public ImageAttachmentException(String arg0) {
		super( arg0 );
	}

	public ImageAttachmentException(Throwable arg0) {
		super( arg0 );
	}

	public ImageAttachmentException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

}
