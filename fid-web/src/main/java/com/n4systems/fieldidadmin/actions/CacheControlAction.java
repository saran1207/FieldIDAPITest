package com.n4systems.fieldidadmin.actions;


import java.util.Collection;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.Initializer;
import com.n4systems.services.SafetyNetworkConnectionCacheInitializer;
import com.n4systems.services.SetupDataLastModUpdateServiceInitializer;
import com.n4systems.services.TenantCache;
import com.n4systems.services.TenantCachePreloader;



public class CacheControlAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CacheControlAction.class);
	
	public String doShow() {
		return SUCCESS;
	}
	
	public String doReloadTenantCache() {
		reload(new TenantCachePreloader(), "Tenant Cache");
		return doShow();
	}
	
	public String doReloadSetupDataLastModDatesCache() {
		reload(new SetupDataLastModUpdateServiceInitializer(), "Setup Data Mod Date Cache");
		return doShow();
	}

	public String doReloadSafetyNetworkConnectionCachae() {
		reload(new SafetyNetworkConnectionCacheInitializer(), "Safety Network Connection Cache");
		return doShow();
	}
	
	private void reload(Initializer init, String name) {
		try {
			init.uninitialize();
			init.initialize();
			addActionMessage("Congrats you reloaded the " + name + "!!");
		} catch(Exception e) {
			addActionError(name + " Reload Failed: " + e.getMessage());
			logger.error("Failed to reaload cache: " + init.getClass().getSimpleName(), e);
		}
	}
	
	public Collection<Tenant> getTenants() {
		return new TreeSet<Tenant>(TenantCache.getInstance().findAllTenants());
	}
	
	public Collection<PrimaryOrg> getPrimaryOrgs() {
		return new TreeSet<PrimaryOrg>(TenantCache.getInstance().findAllPrimaryOrgs());
	}
}
