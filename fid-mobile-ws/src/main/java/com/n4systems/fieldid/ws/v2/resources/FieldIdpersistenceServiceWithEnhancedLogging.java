package com.n4systems.fieldid.ws.v2.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.parents.AbstractEntity;
import com.newrelic.api.agent.NewRelic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class FieldIdpersistenceServiceWithEnhancedLogging<A, E extends AbstractEntity> extends FieldIdPersistenceService {

    @Autowired private HttpServletRequest request;

    public void setEnhancedLoggingCustomParameters() {
        setEnhancedLoggingCustomParameters(getCurrentTenant().getName(), getCurrentUser().getUserID());
    }

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
