package com.n4systems.services;

import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.persistence.loaders.AllEntityListLoader;

public class SafetyNetworkConnectionCacheInitializer implements Initializer {

	public void initialize() {
		SafetyNetworkSecurityCache cache = SafetyNetworkSecurityCache.getInstance();

		AllEntityListLoader<OrgConnection> connectionLoader = new AllEntityListLoader<OrgConnection>(OrgConnection.class);
		
		for (OrgConnection conn: connectionLoader.load()) {
			cache.connect(conn);
		}
	}

	public void uninitialize() {
		SafetyNetworkSecurityCache.getInstance().clear();
	}

}
