package com.n4systems.exceptions.asset;

import com.n4systems.exceptions.InvalidArgumentException;

public class DuplicateAssetException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public DuplicateAssetException() {
	}

	public DuplicateAssetException(String message) {
		super(message);
	}

	public DuplicateAssetException(Throwable cause) {
		super(cause);
	}

	public DuplicateAssetException(String message, Throwable cause) {
		super(message, cause);
	}

}
