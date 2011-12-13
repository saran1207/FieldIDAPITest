package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.core.Response.Status;

public class NotFoundException extends ApiException {

	public NotFoundException() {
		super(Status.NOT_FOUND);
	}
	
	public NotFoundException(String message) {
		super(Status.NOT_FOUND, message);
	}
	
	public NotFoundException(String resource, Object id) {
		this(resource + " not found at [" + String.valueOf(id) + "]");
	}
}
