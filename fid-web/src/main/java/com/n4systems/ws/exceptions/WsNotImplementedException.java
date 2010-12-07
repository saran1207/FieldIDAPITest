package com.n4systems.ws.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class WsNotImplementedException extends WsException {

	public WsNotImplementedException() {
		super(Response.status(501).entity("Not Implemented").type(MediaType.TEXT_PLAIN_TYPE).build());
	}

}
