package com.n4systems.exceptions;

import com.n4systems.model.AssetType;

public class InfoFieldNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public InfoFieldNotFoundException() {}

	public InfoFieldNotFoundException(String message) {
		super(message);
	}
	
	public InfoFieldNotFoundException(String fieldName, AssetType type) {
		this(fieldName, type, null);
	}
	
	public InfoFieldNotFoundException(String fieldName, AssetType type, Throwable cause) {
		super("Could not find InfoField [" + fieldName + "] on AssetType [" + type.getName() + "]", cause);
	}

}
