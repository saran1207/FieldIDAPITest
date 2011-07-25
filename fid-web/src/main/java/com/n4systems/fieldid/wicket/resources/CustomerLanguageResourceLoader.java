package com.n4systems.fieldid.wicket.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;

public class CustomerLanguageResourceLoader implements IStringResourceLoader {

    private static Properties jobsiteProperties;
    private static Properties customerProperties;

    static {
        jobsiteProperties = loadLanguageProperties("jobsite-language");
        customerProperties = loadLanguageProperties("customer-language");
    }

    @Override
    public String loadStringResource(Class<?> clazz, String key, Locale locale, String style) {
        return loadResource(key);
    }

    @Override
    public String loadStringResource(Component component, String key) {
        return loadResource(key);
    }

    private String loadResource(String key) {
    	SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();
        boolean useJobsites = (securityGuard != null) ? securityGuard.isJobSitesEnabled() : false;
        if (useJobsites) {
            return jobsiteProperties.getProperty(key);
        } else {
            return customerProperties.getProperty(key);
        }
    }

    private static Properties loadLanguageProperties(String name) {
        try {
            InputStream is = CustomerLanguageResourceLoader.class.getResourceAsStream("/com/n4systems/fieldid/lang/" + name + ".properties");
            Properties languageProperties = new Properties();
            languageProperties.load(is);
            return languageProperties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
