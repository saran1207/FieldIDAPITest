package com.n4systems.fieldid.ws.v2.resources;

import com.newrelic.api.agent.NewRelic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class EnhancedLogging {

    @Autowired private HttpServletRequest request;

    public void setEnhancedLoggingCustomParameters(String currentTenant, String currentUser) {
        NewRelic.addCustomParameter("Tenant", currentTenant);
        NewRelic.addCustomParameter("User", currentUser);
    }

    public void setEnhancedLoggingWithAppInfoParameters(String currentTenant, String currentUser) {
        setEnhancedLoggingCustomParameters(currentTenant, currentUser);
        setEnhancedLoggingAppInfoParameters();
    }

    public void setEnhancedLoggingAppInfoParameters() {
        NewRelic.addCustomParameter("Device", request.getHeader("X-APPINFO-DEVICE"));
        NewRelic.addCustomParameter("Device Type", request.getHeader("X-APPINFO-DEVICETYPE"));
        NewRelic.addCustomParameter("Platform", request.getHeader("X-APPINFO-PLATFORM"));
        NewRelic.addCustomParameter("OS Version", request.getHeader("X-APPINFO-OSVERSION"));
        NewRelic.addCustomParameter("AppInfo", request.getHeader("X-APPINFO-APPVERSION"));
    }

}
