package com.n4systems.ws.exceptions;

import javax.ws.rs.core.Response.Status;

public class WsInternalErrorException extends WsException {

	public WsInternalErrorException(String message) {
		super(Status.INTERNAL_SERVER_ERROR, message);
	}

	public WsInternalErrorException() {
		super(Status.INTERNAL_SERVER_ERROR);
	}

}
