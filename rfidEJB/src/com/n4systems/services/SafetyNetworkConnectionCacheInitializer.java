package com.n4systems.services;

import org.apache.log4j.Logger;

import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.persistence.loaders.AllEntityListLoader;

public class SafetyNetworkConnectionCacheInitializer implements Initializer {
	private Logger logger = Logger.getLogger(SafetyNetworkConnectionCacheInitializer.class);
	
	public void initialize() {
		logger.info("Pre-Loading SafetyNetworkSecurityCache ... ");
		SafetyNetworkSecurityCache cache = SafetyNetworkSecurityCache.getInstance();

		AllEntityListLoader<OrgConnection> connectionLoader = new AllEntityListLoader<OrgConnection>(OrgConnection.class);
		
		for (OrgConnection conn: connectionLoader.load()) {
			cache.connect(conn);
		}
		logger.info("Complete");
	}

	public void uninitialize() {
		logger.info("Clearing SafetyNetworkSecurityCache... ");
		SafetyNetworkSecurityCache.getInstance().clear();
		logger.info("Complete");
	}

}
