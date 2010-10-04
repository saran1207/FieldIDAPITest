package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;

public enum SecurityLevel {

	LOCAL, LOCAL_ENDUSER, SAFETY_NETWORK;

	public boolean isLocal() {
		return this == LOCAL;
	}

	public static SecurityLevel calculateSecurityLevel(BaseOrg from, BaseOrg to) {
		if (from.getTenant().equals(to.getTenant())) {
            if (from.isExternal()) {
			    return SecurityLevel.LOCAL_ENDUSER;
            } else {
                return SecurityLevel.LOCAL;
            }
		}
        return SecurityLevel.SAFETY_NETWORK;
    }
	
}
