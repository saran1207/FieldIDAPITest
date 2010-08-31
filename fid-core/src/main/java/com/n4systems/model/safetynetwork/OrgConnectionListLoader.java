package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

/**
 * Abstract loader containing the logic for securely loading customer or vendor OrgConnections.
 * Sub classes must specify which list they are looking for, a customer list of vendor list.
 * The security filtering will be applied to the side opposite the list requested.
 * 
 * @see VendorOrgConnectionsListLoader
 * @see CustomerOrgConnectionsListLoader
 */
public class OrgConnectionListLoader extends ListLoader<OrgConnection> {
	private final OrgConnectionType connectionListType;

	public OrgConnectionListLoader(SecurityFilter filter, OrgConnectionType connectionListType) {
		super(filter);
		this.connectionListType = connectionListType;
	}

	@Override
	protected List<OrgConnection> load(EntityManager em, SecurityFilter filter) {
		List<OrgConnection> connections = OrgConnectionQueryBuilderFactory.createAllConnectionsQuery(filter, connectionListType).getResultList(em);
		return connections;
	}
	
	public OrgConnectionType getConnectionListType() {
		return connectionListType;
	}
	
}
