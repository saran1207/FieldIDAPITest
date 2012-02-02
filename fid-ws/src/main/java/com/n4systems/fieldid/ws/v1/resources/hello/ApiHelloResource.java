package com.n4systems.fieldid.ws.v1.resources.hello;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Path("hello")
public class ApiHelloResource {
	private Logger logger = Logger.getLogger(ApiHelloResource.class);
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	public void hello(
			@QueryParam("tenant") String tenant,
			@QueryParam("user") String user,
			@QueryParam("version") String version,
			@QueryParam("device") String device) {

		logger.info(String.format("Hello: [%s:%s] [%s] [%s]", tenant, user, version, device));
	}
	
}
