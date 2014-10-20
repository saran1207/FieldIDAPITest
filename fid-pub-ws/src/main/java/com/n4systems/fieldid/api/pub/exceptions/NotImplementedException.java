package com.n4systems.fieldid.api.pub.exceptions;

import javax.ws.rs.core.Response;

public class NotImplementedException extends ApiException {

	public NotImplementedException() {
		super(Response.Status.NOT_IMPLEMENTED);
	}

	public NotImplementedException(String message) {
		super(Response.Status.NOT_IMPLEMENTED, message);
	}
}