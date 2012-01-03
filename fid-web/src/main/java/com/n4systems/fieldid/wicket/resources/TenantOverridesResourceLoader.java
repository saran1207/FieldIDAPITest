package com.n4systems.fieldid.wicket.resources;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import java.util.Locale;
import java.util.Map;

public class TenantOverridesResourceLoader implements IStringResourceLoader {

    @Override
    public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
        return loadTenantOverride(key);
    }

    @Override
    public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
        return loadTenantOverride(key);
    }

    private String loadTenantOverride(String key) {
    	Map<String, String> tenantOverrides = FieldIDSession.get().getTenantLangOverrides();
        return (tenantOverrides != null) ? tenantOverrides.get(key) : null;
    }

}
