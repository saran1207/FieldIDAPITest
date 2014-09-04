package com.n4systems.fieldid.api.pub.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class ApiException extends WebApplicationException {

	public ApiException(Response.Status status) {
		this(status, "");
	}

	public ApiException(Response.Status status, String message) {
		this(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build());
	}

	public ApiException(Response response) {
		super(response);
	}
}
