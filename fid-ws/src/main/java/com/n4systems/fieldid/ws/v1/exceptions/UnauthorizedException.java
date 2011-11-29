package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.core.Response.Status;

public class UnauthorizedException extends ApiException {

	public UnauthorizedException() {
		super(Status.UNAUTHORIZED);
	}
	
	public UnauthorizedException(String message) {
		super(Status.UNAUTHORIZED, message);
	}

}
