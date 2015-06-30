package com.n4systems.fieldid.api.mobile.filters;

import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.fieldid.version.FieldIdVersion;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class ServerInformationResponseFilter extends FieldIdService implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if (securityContext.hasUserSecurityFilter()) {
			responseContext.getHeaders().add("X-TENANT", securityContext.getUserSecurityFilter().getUser().getTenant().getMobileId());
		}
		responseContext.getHeaders().add("X-Timestamp", new Date().getTime());
		responseContext.getHeaders().add("Server", "Field ID/" + FieldIdVersion.getVersion());
	}
}
