package com.n4systems.ws.resources;

import com.n4systems.ws.utils.TenantNameUriParser;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/ErrorReporting")
public class MobileErrorReportingResource {
	private static Logger logger = Logger.getLogger("MobileErrorLogger");
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String list(@Context UriInfo uriInfo, 
			@FormParam("userId") String userId,
			@FormParam("username") String username,			
			@FormParam("tenantId") String tenantId,
			@FormParam("errorMessage") String errorMessage,
			@FormParam("date") String date,
			@FormParam("softwareVersion") String softwareVersion,
			@FormParam("deviceId") String deviceId) {
		
		TenantNameUriParser uriParser = new TenantNameUriParser();
		
		String tenantName = uriParser.parseTenantName(uriInfo.getRequestUri());
		
		logger.info(deviceId+" : "+
				   softwareVersion +" : "+
				   tenantName +" ("+
				   tenantId+") : "+
				   username+" ("+
				   userId+")" + " : " + 
				   date + " : " + 
				   errorMessage);
		
		return "OK";
	}
	
}
