package com.n4systems.fieldid.api.pub.config;

import com.n4systems.fieldid.service.SecurityContextInitializer;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Scope("singleton")
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

		logger.error("Uncaught exception in webservice", exception);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).build();
	}
}
