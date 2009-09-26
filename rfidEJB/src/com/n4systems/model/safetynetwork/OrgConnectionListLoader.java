package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * Abstract loader containing the logic for securely loading customer or vendor OrgConnections.
 * Sub classes must specify which list they are looking for, a customer list of vendor list.
 * The security filtering will be applied to the side opposite the list requested.
 * 
 * @see VendorOrgConnectionsListLoader
 * @see CustomerOrgConnectionsListLoader
 */
public class OrgConnectionListLoader extends ListLoader<OrgConnection> {
	protected static final boolean CUSTOMER_LIST = true;
	protected static final boolean VENDOR_LIST = false; 
	
	private final OrgConnectionType connectionListType;
	
	public OrgConnectionListLoader(SecurityFilter filter, OrgConnectionType connectionListType) {
		super(filter);
		this.connectionListType = connectionListType;
	}

	@Override
	protected List<OrgConnection> load(EntityManager em, SecurityFilter filter) {
		if (filter.getOwner() == null) {
			throw new SecurityException("SecurityFilter owner must be set to use OrgConnectionListLoader");
		}
		
		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class);

		if (connectionListType == OrgConnectionType.CUSTOMER) {
			builder.addSimpleWhere("vendor.id", filter.getOwner().getId());
		} else {
			builder.addSimpleWhere("customer.id", filter.getOwner().getId());
		}

		List<OrgConnection> connections = builder.getResultList(em);
		return connections;
	}
	
}
