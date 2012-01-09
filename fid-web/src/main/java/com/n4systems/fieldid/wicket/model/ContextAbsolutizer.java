package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

public class ContextAbsolutizer {

    public static String absolutize(String path) {
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

}
