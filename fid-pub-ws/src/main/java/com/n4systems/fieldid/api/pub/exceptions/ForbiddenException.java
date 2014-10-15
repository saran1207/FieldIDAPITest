package com.n4systems.fieldid.api.pub.exceptions;

import javax.ws.rs.core.Response;

public class ForbiddenException extends ApiException {

	public ForbiddenException() {
		super(Response.Status.FORBIDDEN);
	}

	public ForbiddenException(String message) {
		super(Response.Status.FORBIDDEN, message);
	}

}
