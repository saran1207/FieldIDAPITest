package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class ApiException extends WebApplicationException {
	
	public ApiException(Status status) {
		this(status, "");
	}
	
	public ApiException(Status status, String message) {
		this(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build());
	}
	
	public ApiException(Response response) {
		super(response);
	}
}
