package com.n4systems.fieldid.config;

import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;

import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.exceptions.UnauthorizedException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class ApiSecurityRequestFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		URI requestPath = request.getAbsolutePath();
		
		// filtering is enabled for the entire api path so we need to skip it for the auth and hello resources
		if (requestPath.getPath().matches(".*/api/.*/(authenticate|hello)$")) {
			return request;
		}
		
		MultivaluedMap<String, String> params = request.getQueryParameters();
		if (!params.containsKey("k")) {
			throw new UnauthorizedException("Requests must contain an auth key (" + request.getAbsolutePath() + "?k=<authKey>)");
		}

		String authKey = params.getFirst("k");
		try {
			SecurityContextInitializer.initSecurityContext(authKey);
		} catch (SecurityException e) {
			throw new ForbiddenException("Invalid auth key '" + authKey + "'");
		}
		
		return request;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		/*
		 * XXX if an exception is thrown during the resource handling, this method won't be called.
		 * The CatchAllExceptionMapper has also been setup to reset the security context in that case. - mf
		 */ 
		SecurityContextInitializer.resetSecurityContext();
		return response;
	}

}
