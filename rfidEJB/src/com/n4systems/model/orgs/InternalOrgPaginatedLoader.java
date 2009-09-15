package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InternalOrgPaginatedLoader extends PaginatedLoader<InternalOrg> {

	public InternalOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected QueryBuilder<InternalOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<InternalOrg> builder = new QueryBuilder<InternalOrg>(InternalOrg.class, filter);
		builder.addOrder("name");
		return builder;
	}

}
