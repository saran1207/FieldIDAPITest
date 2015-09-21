package com.n4systems.fieldid.ws.v2.filters;

import com.n4systems.fieldid.service.SecurityContextInitializer;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		URI requestPath = request.getUriInfo().getAbsolutePath();

		// Whitelisted paths
		if (requestPath.getPath().matches(".*/(authenticate.*|hello|log)$")) {
			return;
		}

		MultivaluedMap<String, String> params = request.getUriInfo().getQueryParameters();
		if (!params.containsKey("k")) {
			request.abortWith(Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("Requests must contain an auth key (" + requestPath + "?k=<authKey>)")
					.build());

			return;
		}

		String authKey = params.getFirst("k");
		try {
			SecurityContextInitializer.initSecurityContext(authKey);
		} catch (SecurityException e) {
			request.abortWith(Response
					.status(Response.Status.FORBIDDEN)
					.entity("Invalid auth key '" + authKey + "'")
					.build());
		}
	}

}
