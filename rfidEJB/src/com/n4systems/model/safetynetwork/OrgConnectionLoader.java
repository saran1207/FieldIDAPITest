package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

/**
 * Abstract loader containing the logic for securely loading a single customer or vendor OrgConnection.
 * Sub classes must specify which list they are looking for, a customer list of vendor list.
 * The security filtering will be applied to the side opposite the list requested.
 * 
 * @see VendorOrgConnectionsLoader
 * @see CustomerOrgConnectionsLoader
 */
public class OrgConnectionLoader extends SecurityFilteredLoader<OrgConnection> {
	private Long linkedOrgId;
	private final OrgConnectionType connectionType;
	
	public OrgConnectionLoader(SecurityFilter filter, OrgConnectionType connectionListType) {
		super(filter);
		this.connectionType = connectionListType;
	}

	@Override
	protected OrgConnection load(EntityManager em, SecurityFilter filter) {
		OrgConnection connection = OrgConnectionQueryBuilderFactory.createSingleQuery(filter, connectionType, linkedOrgId).getSingleResult(em);
		return connection;
	}
	
	public OrgConnectionLoader setLinkedOrgId(Long linkedOrgId) {
		this.linkedOrgId = linkedOrgId;
		return this;
	}
	
	public Long getLinkedOrgId() {
		return linkedOrgId;
	}
	
	public OrgConnectionType getConnectionType() {
		return connectionType;
	}
}
