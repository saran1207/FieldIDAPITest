package com.n4systems.model.orgs;

import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class SecondaryOrgPaginatedLoader extends PaginatedLoader<SecondaryOrg> {

	public SecondaryOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<SecondaryOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<SecondaryOrg> builder = new QueryBuilder<SecondaryOrg>(SecondaryOrg.class, filter.prepareFor(SecondaryOrg.class));
		builder.addOrder("name");
		return builder;
	}

}
