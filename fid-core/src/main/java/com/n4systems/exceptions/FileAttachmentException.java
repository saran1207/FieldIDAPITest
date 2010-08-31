package com.n4systems.exceptions;

public class FileAttachmentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileAttachmentException() {
	}

	public FileAttachmentException(String arg0) {
		super( arg0 );
	}

	public FileAttachmentException(Throwable arg0) {
		super( arg0 );
	}

	public FileAttachmentException(String arg0, Throwable arg1) {
		super( arg0, arg1 );
	}

}
