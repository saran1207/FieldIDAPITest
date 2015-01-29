package com.n4systems.fieldid.api.pub.filters;

import com.n4systems.fieldid.service.SecurityContextInitializer;
import org.apache.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Throwable> {
	private Logger logger = Logger.getLogger(CatchAllExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception) {
		/*
		 * XXX: due to the Jersey filter design, the response filer cannot reset the security context if an exception is thrown.
		 * It is important we do this here, as it's probable our last chance to clear it. - mf
		 */
		SecurityContextInitializer.resetSecurityContext();

		logger.error("Exception in webservice", exception);

		if (exception instanceof WebApplicationException) {
			return ((WebApplicationException) exception).getResponse();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).build();
		}
	}
}
