package com.n4systems.ws.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class WsException extends WebApplicationException {
	
	protected WsException(Status status) {
		this(status, "");
	}
	
	protected WsException(Status status, String message) {
		this(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build());
	}
	
	protected WsException(Response response) {
		super(response);
	}
}
