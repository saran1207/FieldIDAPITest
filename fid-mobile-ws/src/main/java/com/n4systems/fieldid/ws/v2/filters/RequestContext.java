package com.n4systems.fieldid.ws.v2.filters;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/*
 This class exposes ContainerRequestContext to spring.  JaxRS and Spring will create separate instances of the class but
 both will share the same ThreadLocal context.  This was created as a workaround to a Spring bug where the HttpServletRequest
 was uninjectable within a JaxRS environment.
 */
@Provider
@PreMatching
public class RequestContext implements ContainerRequestFilter, ContainerResponseFilter {

	private static final ThreadLocal<ContainerRequestContext> context = new ThreadLocal<>();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		context.set(requestContext);
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		context.remove();
	}

	public ContainerRequestContext getContext() {
		ContainerRequestContext ctx = context.get();
		if (ctx == null) {
			throw new IllegalStateException("RequestContext accessed outside request scope");
		}
		return ctx;
	}
}
