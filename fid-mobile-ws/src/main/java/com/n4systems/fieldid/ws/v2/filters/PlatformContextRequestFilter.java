package com.n4systems.fieldid.ws.v2.filters;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.PlatformType;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.USER)
public class PlatformContextRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		String device = request.getHeaderString("X-APPINFO-DEVICE");

		if(device != null && !device.equals("null")) {
			String deviceType = request.getHeaderString("X-APPINFO-DEVICETYPE");
			String platform = request.getHeaderString("X-APPINFO-PLATFORM");
			String osVersion = request.getHeaderString("X-APPINFO-OSVERSION");
			String appVersion = request.getHeaderString("X-APPINFO-APPVERSION");
			String notes = request.getHeaderString("X-APPINFO-NOTES");

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
		}
	}
}
