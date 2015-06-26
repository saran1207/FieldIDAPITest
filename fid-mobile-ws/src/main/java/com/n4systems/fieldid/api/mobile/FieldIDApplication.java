package com.n4systems.fieldid.api.mobile;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class FieldIDApplication extends ResourceConfig {

	public FieldIDApplication() {
		packages("com.n4systems.fieldid.api.mobile");
		register(RequestContextFilter.class);
		register(JacksonFeature.class);
		EncodingFilter.enableFor(this, GZipEncoder.class);
	}

}