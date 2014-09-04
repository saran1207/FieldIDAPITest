package com.n4systems.fieldid.api.pub;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class FieldIDApplication extends ResourceConfig {

	public FieldIDApplication() {
		packages("com.n4systems.fieldid.api.pub");
	}

}