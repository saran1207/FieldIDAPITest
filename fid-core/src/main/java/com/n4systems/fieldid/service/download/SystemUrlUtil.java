package com.n4systems.fieldid.service.download;

import com.n4systems.model.Tenant;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;

public class SystemUrlUtil {

    public static String getSystemUrl(Tenant tenant) {
        String absoluteUrl = ConfigService.getInstance().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://";
        absoluteUrl += tenant.getName() + ".";
        absoluteUrl += ConfigService.getInstance().getString(ConfigEntry.SYSTEM_DOMAIN);
        return absoluteUrl;
    }

}
