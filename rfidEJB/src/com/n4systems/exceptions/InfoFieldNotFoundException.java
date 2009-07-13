package com.n4systems.exceptions;

import com.n4systems.model.ProductType;

public class InfoFieldNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public InfoFieldNotFoundException() {}

	public InfoFieldNotFoundException(String message) {
		super(message);
	}
	
	public InfoFieldNotFoundException(String fieldName, ProductType type) {
		this(fieldName, type, null);
	}
	
	public InfoFieldNotFoundException(String fieldName, ProductType type, Throwable cause) {
		super("Could not find InfoField [" + fieldName + "] on ProductType [" + type.getName() + "]", cause);
	}

}
