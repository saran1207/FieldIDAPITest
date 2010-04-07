package com.n4systems.model.tenant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class TenantNameAvailabilityChecker {
	private final Logger logger = Logger.getLogger(TenantNameAvailabilityChecker.class);
	private final AllEntityListLoader<Tenant> tenantsLoader;
	private final ConfigContext configContext;
	private final File reservedTenantNamesFile;
	
	private Set<String> unavailableNames;
	
	public TenantNameAvailabilityChecker() {
		this(new AllEntityListLoader<Tenant>(Tenant.class), ConfigContext.getCurrentContext(), PathHandler.getReservedTenantNamesConfigFile());
	}
	
	public TenantNameAvailabilityChecker(AllEntityListLoader<Tenant> tenantsLoader, ConfigContext configContext, File reservedTenantNamesFile) {
		this.tenantsLoader = tenantsLoader;
		this.configContext = configContext;
		this.reservedTenantNamesFile = reservedTenantNamesFile;
	}
	
	public boolean isAvailable(String name) {
		if (unavailableNames == null) {
			loadUnavailableNames();
		}
		
		boolean available = !unavailableNames.contains(name.trim().toLowerCase());
		return available;
	}
	
	private void loadUnavailableNames() {
		Set<String> rawNames = new HashSet<String>();
		rawNames.addAll(loadReservedNames());
		rawNames.addAll(loadNamesInUse());
		rawNames.add(loadUnbrandedSubDomainName());
		
		unavailableNames = new HashSet<String>(rawNames.size());
		for (String name: rawNames) {
			unavailableNames.add(name.trim().toLowerCase());
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<String> loadReservedNames() {
		InputStream in = null;
		try {
			in = new FileInputStream(reservedTenantNamesFile);
			
			return IOUtils.readLines(in);	
		} catch (IOException e) {
			logger.warn("Could not load reserved tenant names", e);
			return new ArrayList<String>();
		} finally {
			StreamUtils.close(in);
		}
	}
	
	private List<String> loadNamesInUse() {
		List<String> tenantNames = new ArrayList<String>();
		
		for (Tenant tenant: tenantsLoader.load()) {
			tenantNames.add(tenant.getName());
		}
		
		return tenantNames;
	}
	
	private String loadUnbrandedSubDomainName() {
		return configContext.getString(ConfigEntry.UNBRANDED_SUBDOMAIN);
	}
}
