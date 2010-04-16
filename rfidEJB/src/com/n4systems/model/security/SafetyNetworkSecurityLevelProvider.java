package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.OrgConnection;

public interface SafetyNetworkSecurityLevelProvider {

	/**
	 * Returns the network distance (SecurityLevel) from one org to another.
	 * @param from	The org to search from
	 * @param to	The org to find the distance to
	 * @return		SecurityLevel representing the distance
	 */
	public SecurityLevel getConnectionSecurityLevel(BaseOrg from, BaseOrg to);

	/** Connects two PrimaryOrgs together in a bi-directional connection. */
	public void connect(OrgConnection conn);

	/** Removes all nodes from the node list */
	public void clear();

	public Integer connectionCount();

	public Boolean isEmpty();

}