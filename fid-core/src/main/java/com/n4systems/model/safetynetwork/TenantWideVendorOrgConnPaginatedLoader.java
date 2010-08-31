package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantWideVendorOrgConnPaginatedLoader extends PaginatedLoader<OrgConnection> {

	public TenantWideVendorOrgConnPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<OrgConnection> createBuilder(SecurityFilter filter) {
		QueryBuilder<OrgConnection> builder = new QueryBuilder<OrgConnection>(OrgConnection.class);

		builder.addSimpleWhere("customer.tenant.id", filter.getTenantId());

		return builder;		
	}
	
}
