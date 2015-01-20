package com.n4systems.fieldid.api.pub;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class FieldIDApplication extends ResourceConfig {

	public FieldIDApplication() {
		packages("com.n4systems.fieldid.api.pub");
		register(RequestContextFilter.class);
		register(JacksonFeature.class);
	}

}