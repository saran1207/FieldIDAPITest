package com.n4systems.fieldid.ws.v1.resources.logging;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Path("/log")
@Component
public class ApiLoggingResource {
	private static Logger logger = Logger.getLogger(ApiLoggingResource.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Transactional(readOnly = true)
	public void logMessage(
			@FormParam("tenant") String tenantName,
			@FormParam("user") String userId, 
			@FormParam("message") String message) {
		
		 logger.info(String.format("[%s:%s]: %s", tenantName, userId, message));
	}
}
