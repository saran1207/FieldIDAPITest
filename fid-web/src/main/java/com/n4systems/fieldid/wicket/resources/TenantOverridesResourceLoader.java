package com.n4systems.fieldid.wicket.resources;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import java.util.Locale;

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
        return FieldIDSession.get().getTenantLangOverrides().get(key);
    }

}
