package com.n4systems.fieldid.wicket.resources;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import com.n4systems.fieldid.wicket.FieldIDSession;

public class TenantOverridesResourceLoader implements IStringResourceLoader {

    @Override
    public String loadStringResource(Class<?> clazz, String key, Locale locale, String style) {
        return loadTenantOverride(key);
    }

    @Override
    public String loadStringResource(Component component, String key) {
        return loadTenantOverride(key);
    }

    private String loadTenantOverride(String key) {
    	Map<String, String> tenantOverrides = FieldIDSession.get().getTenantLangOverrides();
        return (tenantOverrides != null) ? tenantOverrides.get(key) : null;
    }

}
