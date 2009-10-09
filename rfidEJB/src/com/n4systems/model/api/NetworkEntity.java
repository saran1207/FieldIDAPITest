package com.n4systems.model.api;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityLevel;

public interface NetworkEntity<T> extends SecurityEnhanced<T> {
	/**
	 * Returns the required security level from the requested fromOrg to this entity's owner.
	 * @param fromOrg	BaseOrg to calculate distance from
	 * @return			SecurityLevel
	 */
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg);
}
