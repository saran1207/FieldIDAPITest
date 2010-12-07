package com.n4systems.ws.exceptions;

import javax.ws.rs.core.Response.Status;

public class WsSecurityException extends WsException {

	public WsSecurityException() {
		super(Status.FORBIDDEN);
	}
	
	public WsSecurityException(String message) {
		super(Status.FORBIDDEN, message);
	}
}
