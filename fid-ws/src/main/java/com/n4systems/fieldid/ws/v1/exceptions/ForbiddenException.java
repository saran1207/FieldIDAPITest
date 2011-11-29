package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.core.Response.Status;

public class ForbiddenException extends ApiException {

	public ForbiddenException() {
		super(Status.FORBIDDEN);
	}
	
	public ForbiddenException(String message) {
		super(Status.FORBIDDEN, message);
	}
	
}
