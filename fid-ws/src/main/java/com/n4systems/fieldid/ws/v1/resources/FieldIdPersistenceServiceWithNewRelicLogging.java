package com.n4systems.fieldid.ws.v1.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.newrelic.api.agent.NewRelic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class FieldIdPersistenceServiceWithNewRelicLogging extends FieldIdPersistenceService {

    @Autowired
    private HttpServletRequest request;

    public void setNewRelicWithAppInfoParameters() {
        setNewRelicWithAppInfoParameters(getCurrentTenant().getName(), getCurrentUser().getUserID());
    }

    public void setNewRelicWithAppInfoParameters(String currentTenant, String currentUser) {
        setNewRelicCustomParameters(currentTenant, currentUser);
        NewRelic.addCustomParameter("Device", request.getHeader("X-APPINFO-DEVICE"));
        NewRelic.addCustomParameter("Device Type", request.getHeader("X-APPINFO-DEVICETYPE"));
        NewRelic.addCustomParameter("Platform", request.getHeader("X-APPINFO-PLATFORM"));
        NewRelic.addCustomParameter("OS Version", request.getHeader("X-APPINFO-OSVERSION"));
        NewRelic.addCustomParameter("AppInfo", request.getHeader("X-APPINFO-APPVERSION"));
    }

}
