package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

/**
 * Abstract loader containing the logic for securely loading a single customer or vendor OrgConnection.
 * Sub classes must specify which list they are looking for, a customer list of vendor list.
 * The security filtering will be applied to the side opposite the list requested.
 * 
 * @see VendorOrgConnectionsLoader
 * @see CustomerOrgConnectionsLoader
 */
public class OrgConnectionByLinkedOrgLoader extends ListLoader<OrgConnection> {
	private Long linkedOrgId;
	private final OrgConnectionType connectionType;
	
	public OrgConnectionByLinkedOrgLoader(SecurityFilter filter, OrgConnectionType connectionListType) {
		super(filter);
		this.connectionType = connectionListType;
	}

	@Override
	protected List<OrgConnection> load(EntityManager em, SecurityFilter filter) {
		List<OrgConnection> connections = OrgConnectionQueryBuilderFactory.createSingleOrgQuery(filter, connectionType, linkedOrgId).getResultList(em);
		return connections;
	}
	
	public OrgConnectionByLinkedOrgLoader setLinkedOrgId(Long linkedOrgId) {
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
