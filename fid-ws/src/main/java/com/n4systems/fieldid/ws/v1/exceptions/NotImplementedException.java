package com.n4systems.fieldid.ws.v1.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotImplementedException extends ApiException {

	public NotImplementedException() {
		super(Response.status(501).entity("Not Implemented").type(MediaType.TEXT_PLAIN_TYPE).build());
	}

}
