package com.n4systems.fieldid.api.pub.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
			MultivaluedMap<String, Object> headers = responseContext.getHeaders();
			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			headers.add("Access-Control-Allow-Headers",
							"Accept, " +
							"Origin, " +
							"Content-Type, " +
							"X-Requested-With, " +
							"X-APPINFO-DEVICE, " +
							"X-APPINFO-DEVICETYPE, " +
							"X-APPINFO-PLATFORM, " +
							"X-APPINFO-OSVERSION, " +
							"X-APPINFO-APPVERSION, " +
							"X-APPINFO-NOTES");
	}
}
