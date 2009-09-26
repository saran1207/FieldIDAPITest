package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

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
	private final OrgConnectionType connectionListType;
	
	public OrgConnectionLoader(SecurityFilter filter, OrgConnectionType connectionListType) {
		super(filter);
		this.connectionListType = connectionListType;
	}

	@Override
	protected OrgConnection load(EntityManager em, SecurityFilter filter) {
		if (filter.getOwner() == null) {
			throw new SecurityException("SecurityFilter owner must be set to use OrgConnectionLoader");
		}
		
		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class);

		if (connectionListType == OrgConnectionType.CUSTOMER) {
			builder.addSimpleWhere("vendor.id", filter.getOwner().getId());
			builder.addSimpleWhere("customer.id", linkedOrgId);
		} else {
			builder.addSimpleWhere("customer.id", filter.getOwner().getId());
			builder.addSimpleWhere("vendor.id", linkedOrgId);
		}

		OrgConnection connection = builder.getSingleResult(em);
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
		return connectionListType;
	}
}
