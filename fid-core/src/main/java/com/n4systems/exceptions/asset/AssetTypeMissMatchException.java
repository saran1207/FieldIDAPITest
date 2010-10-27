package com.n4systems.exceptions.asset;

import com.n4systems.exceptions.InvalidArgumentException;

public class AssetTypeMissMatchException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public AssetTypeMissMatchException() {
	}

	public AssetTypeMissMatchException(String message) {
		super(message);
	}

	public AssetTypeMissMatchException(Throwable cause) {
		super(cause);
	}

	public AssetTypeMissMatchException(String message, Throwable cause) {
		super(message, cause);
	}

}
