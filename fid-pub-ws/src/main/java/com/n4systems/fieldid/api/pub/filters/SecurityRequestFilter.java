package com.n4systems.fieldid.api.pub.filters;

import com.n4systems.fieldid.api.pub.exceptions.UnauthorizedException;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.model.PlatformType;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.apache.log4j.Logger;

public class SecurityRequestFilter  implements ContainerRequestFilter, ContainerResponseFilter {
	private static Logger logger = Logger.getLogger(SecurityRequestFilter.class);

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		String apiKey = request.getHeaderValue("api-key");
		if (apiKey == null || apiKey.length() == 0) {
			throw new UnauthorizedException("Requests must contain the api-key header");
		}

//		String authKey = params.getFirst("k");
//		try {
//			SecurityContextInitializer.initSecurityContext(authKey);
//		} catch (SecurityException e) {
//			throw new ForbiddenException("Invalid auth key '" + authKey + "'");
//		}

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
