package com.n4systems.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.n4systems.ws.utils.TenantNameUriParser;

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
