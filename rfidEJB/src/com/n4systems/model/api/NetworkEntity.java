package com.n4systems.model.api;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.SecurityLevel;

public interface NetworkEntity<T> extends SecurityEnhanced<T> {
	/**
	 * Returns the required security level from the requested fromOrg to this entity's owner.
	 * @param fromOrg	InternalOrg to calculate distance from
	 * @return			SecurityLevel
	 */
	public SecurityLevel getSecurityLevel(InternalOrg fromOrg);
}
