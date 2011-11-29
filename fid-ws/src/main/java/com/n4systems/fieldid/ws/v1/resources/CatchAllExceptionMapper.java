package com.n4systems.fieldid.ws.v1.resources;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Throwable> {
	private Logger logger = Logger.getLogger(CatchAllExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable exception) {
		logger.error("Uncaught exception in webservice", exception);
		return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}
