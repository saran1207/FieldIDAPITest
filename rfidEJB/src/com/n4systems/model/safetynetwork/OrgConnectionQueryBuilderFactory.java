package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class OrgConnectionQueryBuilderFactory {

	public static QueryBuilder<OrgConnection> getQueryBuilder(SecurityFilter filter, OrgConnectionType connectionType) {
		if (filter.getOwner() == null) {
			throw new SecurityException("SecurityFilter owner must be set to use OrgConnectionListLoader");
		}
		
		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class);

		if (connectionType.isCustomer()) {
			builder.addSimpleWhere("vendor.id", filter.getOwner().getId());
		} else {
			builder.addSimpleWhere("customer.id", filter.getOwner().getId());
		}

		return builder;		
	}
	
}
