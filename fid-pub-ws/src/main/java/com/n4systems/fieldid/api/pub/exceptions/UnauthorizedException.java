package com.n4systems.fieldid.api.pub.exceptions;

import javax.ws.rs.core.Response;

public class UnauthorizedException extends ApiException {

	public UnauthorizedException() {
		super(Response.Status.UNAUTHORIZED);
	}

	public UnauthorizedException(String message) {
		super(Response.Status.UNAUTHORIZED, message);
	}

}
