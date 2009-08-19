package com.n4systems.services;

import org.apache.log4j.Logger;

public class TenantCachePreloader implements Initializer {
	private static Logger logger = Logger.getLogger(TenantCachePreloader.class);
	
	public TenantCachePreloader() {}
	
	public void initialize() {
		logger.info("Pre-Loading Tenant Cache ... ");
		TenantCache.getInstance().reloadAll();
		logger.info("Complete");
	}

	public void uninitialize() {
	}

}
