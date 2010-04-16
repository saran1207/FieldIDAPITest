package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.OrgConnection;

/**
 * A cache view of all safety network connections. this class provides singleton access 
 * to the systems SafetyNetworkSecurityLevelProvider.  It is guaranteed to be thread safe.
 * 
 * 
 */
public class SafetyNetworkSecurityCache {
	private static SafetyNetworkSecurityLevelProvider self;
	
	public static SafetyNetworkSecurityLevelProvider getInstance() {
		return self;
	}
	
	/**
	 * Convenience method for {@link SafetyNetworkSecurityCache#getInstance()#getConnectionSecurityLevel(InternalOrg, InternalOrg)}
	 */
	public static SecurityLevel getSecurityLevel(BaseOrg from, BaseOrg to) {
		return self.getConnectionSecurityLevel(from, to);
	}
	
	public static void recordConnection(OrgConnection connection) {
		self.connect(connection);
	}
	
	
	public static void initialize() {
		self = new ThreadSafeSafetyNetworkSecurityLevelProvider();
		
	}
}
