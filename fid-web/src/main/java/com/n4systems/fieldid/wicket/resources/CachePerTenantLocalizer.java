package com.n4systems.fieldid.wicket.resources;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import rfid.web.helper.SessionUser;

public class CachePerTenantLocalizer extends Localizer {

    @Override
    protected String getCacheKey(String key, Component component) {
        String keyPrepend = "";
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        if (sessionUser != null && sessionUser.getTenant() != null) {
            keyPrepend = sessionUser.getTenant().getId().toString();
        }
        return keyPrepend + super.getCacheKey(key, component);
    }

}
