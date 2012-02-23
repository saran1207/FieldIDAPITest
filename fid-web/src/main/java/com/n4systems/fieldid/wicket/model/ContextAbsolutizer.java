package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

public class ContextAbsolutizer {

    public static String toAbsoluteUrl(String path) {
        return ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" +
                determineServerName() +
                toContextAbsoluteUrl(path);
    }


    public static String toContextAbsoluteUrl(String path) {
        String context = determineContext();

        if (!path.startsWith("/")) {
			context += '/';
		}

        return context + path;
    }

    private static String determineContext() {
        ServletWebRequest request = (ServletWebRequest) RequestCycle.get().getRequest();
        return request.getContainerRequest().getContextPath();
    }

    private static String determineServerName() {
        ServletWebRequest request = (ServletWebRequest) RequestCycle.get().getRequest();
        return request.getContainerRequest().getContextPath();
    }


}
