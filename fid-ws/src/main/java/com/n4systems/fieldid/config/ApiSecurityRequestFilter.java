package com.n4systems.fieldid.config;

import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.exceptions.UnauthorizedException;
import com.n4systems.model.PlatformType;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class ApiSecurityRequestFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private static Logger logger = Logger.getLogger(ApiSecurityRequestFilter.class);

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		URI requestPath = request.getAbsolutePath();
		
		// filtering is enabled for the entire api path so we need to skip it for the auth and hello resources
		if (requestPath.getPath().matches(".*/api/.*/(authenticate.*|hello|log)$")) {
			return request;
		}
		
		MultivaluedMap<String, String> params = request.getQueryParameters();
		if (!params.containsKey("k")) {
			throw new UnauthorizedException("Requests must contain an auth key (" + request.getAbsolutePath() + "?k=<authKey>)");
		}

		String authKey = params.getFirst("k");
		try {
			SecurityContextInitializer.initSecurityContext(authKey);
		} catch (SecurityException e) {
			throw new ForbiddenException("Invalid auth key '" + authKey + "'");
		}
		
		setPlatformContext(request);
		
		return request;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		/*
		 * XXX if an exception is thrown during the resource handling, this method won't be called.
		 * The CatchAllExceptionMapper has also been setup to reset the security context in that case. - mf
		 */ 
		SecurityContextInitializer.resetSecurityContext();
		return response;
	}
	
	private void setPlatformContext(ContainerRequest request) {
		// Note: getHeaderValue returns "null" string instead of null.
		String device = request.getHeaderValue("X-APPINFO-DEVICE");
		
		if(device != null && !device.equals("null")) {
			String deviceType = request.getHeaderValue("X-APPINFO-DEVICETYPE");
			String platform = request.getHeaderValue("X-APPINFO-PLATFORM");
			String osVersion = request.getHeaderValue("X-APPINFO-OSVERSION");
			String appVersion = request.getHeaderValue("X-APPINFO-APPVERSION");
			String notes = request.getHeaderValue("X-APPINFO-NOTES");
			
			StringBuffer sb = new StringBuffer();
	        if (platform != null && !platform.equals("null")) {
	            sb.append(platform).append(", ");
	        }
	        if (appVersion != null && !appVersion.equals("null")) {
	            sb.append(appVersion).append(" ");
	        }
	        sb.append(device).append(" ");
	        if (deviceType != null && !deviceType.equals("null")) {
	            sb.append(deviceType).append(" ");
	        }
	        if (osVersion != null && !osVersion.equals("null")) {
	            sb.append(osVersion).append(" ");
	        }
	        if (notes != null && !notes.equals("null")) {
	            sb.append(notes);
	        }
			
			ThreadLocalInteractionContext.getInstance().setCurrentPlatform(sb.toString().trim());
            ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(PlatformType.MOBILE);
			
			logger.info("PlatformContext set for device: " + device);
		}
	}
}
