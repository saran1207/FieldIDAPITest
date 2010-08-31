package com.n4systems.exceptions;

import rfid.ejb.entity.InfoFieldBean;

public class InfoOptionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public InfoOptionNotFoundException() {}

	public InfoOptionNotFoundException(String fieldName) {
		super("Could not find InfoOption [" + fieldName + "]");
	}
	
	public InfoOptionNotFoundException(String fieldName, InfoFieldBean field) {
		this(fieldName, field, null);
	}
	
	public InfoOptionNotFoundException(String fieldName, InfoFieldBean field, Throwable cause) {
		super("Could not find static InfoOption [" + fieldName + "] on InfoField [" + field.getName() + "]", cause);
	}

}
