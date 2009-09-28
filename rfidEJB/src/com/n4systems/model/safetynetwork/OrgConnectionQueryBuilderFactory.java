package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.OrgConnectionSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;

public class OrgConnectionQueryBuilderFactory {

	private static void checkFilter(SecurityFilter filter) {
		if (filter.getOwner() == null) {
			throw new SecurityException("SecurityFilter owner must be set to use OrgConnectionListLoader");
		}
	}
	
	public static QueryBuilder<OrgConnection> createListQuery(SecurityFilter filter, OrgConnectionType connectionType) {
		checkFilter(filter);
		
		// create this query builder using an OrgConnection security filter.  note that we apply the security to the opposite side since 
		// if we want our customer list, then we're the vendor (and via versa)
		QueryFilter orgConnFilter = new OrgConnectionSecurityFilter(connectionType.getOppositeType(), filter);
		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class, orgConnFilter);

		return builder;
	}
	
	public static QueryBuilder<OrgConnection> createSingleQuery(SecurityFilter filter, OrgConnectionType connectionType, Long linkedOrgId) {
		QueryBuilder<OrgConnection> builder = createListQuery(filter, connectionType);

		if (connectionType.isCustomer()) {
			builder.addSimpleWhere("customer.id", linkedOrgId);
		} else {
			builder.addSimpleWhere("vendor.id", linkedOrgId);
		}

		return builder;
	}
}