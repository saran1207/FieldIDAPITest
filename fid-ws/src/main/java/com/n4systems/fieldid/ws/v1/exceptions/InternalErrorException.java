package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.core.Response.Status;

public class InternalErrorException extends ApiException {

	public InternalErrorException(String message) {
		super(Status.INTERNAL_SERVER_ERROR, message);
	}

	public InternalErrorException() {
		super(Status.INTERNAL_SERVER_ERROR);
	}

}
