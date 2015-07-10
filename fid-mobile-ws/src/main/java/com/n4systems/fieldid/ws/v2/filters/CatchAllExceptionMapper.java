package com.n4systems.fieldid.ws.v2.filters;

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

	class JsonErrorResponse {
		final int status;
		final String message;

		JsonErrorResponse(int status, String message) {
			this.status = status;
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}
	}


	@Override
	public Response toResponse(Throwable exception) {
		/*
		 * XXX: due to the Jersey filter design, the response filer cannot reset the security context if an exception is thrown.
		 * It is important we do this here, as it's probable our last chance to clear it. - mf
		 */
		SecurityContextInitializer.resetSecurityContext();

		int status;
		if (exception instanceof WebApplicationException) {
			logger.warn(exception.getMessage());
			status = ((WebApplicationException) exception).getResponse().getStatus();
		} else {
			logger.error("Unhandled exception in mobile webservice", exception);
			status = 500;
		}

		JsonErrorResponse resp = new JsonErrorResponse(status, exception.getMessage());
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(resp).build();
	}
}
