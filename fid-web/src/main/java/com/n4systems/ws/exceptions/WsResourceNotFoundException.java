package com.n4systems.ws.exceptions;

import javax.ws.rs.core.Response.Status;

public class WsResourceNotFoundException extends WsException {

	public WsResourceNotFoundException() {
		super(Status.NOT_FOUND);
	}
	
	public WsResourceNotFoundException(String message) {
		super(Status.NOT_FOUND, message);
	}
	
}
