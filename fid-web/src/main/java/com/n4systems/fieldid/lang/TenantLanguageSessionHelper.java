package com.n4systems.fieldid.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.fieldid.actions.utils.WebSession;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.TenantFinder;

public class TenantLanguageSessionHelper {
	private static Logger logger = Logger.getLogger(TenantLanguageSessionHelper.class);
	private static final String JOBSITE_PROPERTIES = "jobsite-language.properties";
	private static final String CUSTOMER_PROPERTIES = "customer-language.properties";
	private static final String PACKAGE_PROPERTIES_PATH = "com.n4systems.fieldid.actions";
	
	private final Tenant tenant;
	
	public TenantLanguageSessionHelper(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public void populateSession(WebSession session) {
		PrimaryOrg primary = TenantFinder.getInstance().findPrimaryOrg(tenant.getId());
		
		Map<String, String> language = new HashMap<String, String>();
		
		// first we need to apply the customer/jobsite language overrides
		if (primary.hasExtendedFeature(ExtendedFeature.JobSites)) {
			language.putAll(loadJobSiteLanguage());
		} else {
			language.putAll(loadCustomerLanguage());
		}
		
		// then we apply customer overrides (in case they have overrides for customer/jobsite as well)
		language.putAll(loadTenantLanguage(tenant));
		
		// populate the session
		session.setTenantLanguageOverrides(language);
	}
	
	private Map<String, String> loadJobSiteLanguage() {
		return loadLanguageFromStream(getClass().getResourceAsStream(JOBSITE_PROPERTIES));
	}
	
	private Map<String, String> loadCustomerLanguage() {
		return loadLanguageFromStream(getClass().getResourceAsStream(CUSTOMER_PROPERTIES));
	}
	
	private Map<String, String> loadLanguageFromStream(InputStream stream) {
		if (stream == null) {
			return new HashMap<String, String>();
		}
		
		Map<String, String> language = new HashMap<String, String>();
		Properties props = new Properties();
		
		try {
			props.load(stream);
		} catch (IOException e) {
			logger.error("Failed to load language overrides", e);
			return new HashMap<String, String>();
		}	
		
		for (Map.Entry<Object, Object> entry: props.entrySet()) {
			language.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}

		return language;
	}
	
	private Map<String, String> loadTenantLanguage(Tenant tenant) {
		File propertiesFile = PathHandler.getPropertiesFile(tenant, Package.getPackage(PACKAGE_PROPERTIES_PATH));
		
		Map<String, String> language;
		if (propertiesFile.exists()) {
			InputStream is = null;
			
			try {
				is = new FileInputStream(propertiesFile);
			} catch (FileNotFoundException e) {}
			
			language = loadLanguageFromStream(is);
			
			IOUtils.closeQuietly(is);
		} else {
			language = new HashMap<String, String>();
		}
		return language;
	}
}
