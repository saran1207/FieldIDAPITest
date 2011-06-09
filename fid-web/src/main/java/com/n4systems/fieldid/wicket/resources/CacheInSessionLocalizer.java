package com.n4systems.fieldid.wicket.resources;

import org.apache.wicket.Localizer;

import java.util.Map;

public class CacheInSessionLocalizer extends Localizer {

    @Override
    protected Map<String, String> newCache() {
        return MapToSessionProxy.createProxy();
    }

}
