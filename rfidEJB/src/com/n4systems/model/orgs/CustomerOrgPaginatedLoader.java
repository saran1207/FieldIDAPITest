package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CustomerOrgPaginatedLoader extends PaginatedLoader<CustomerOrg> {

	public CustomerOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<CustomerOrg> createBuilder(SecurityFilter filter) {
		return new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
	}

}
