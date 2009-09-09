package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class DivisionOrgPaginatedLoader extends PaginatedLoader<DivisionOrg> {

	public DivisionOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected QueryBuilder<DivisionOrg> createBuilder(SecurityFilter filter) {
		return new QueryBuilder<DivisionOrg>(DivisionOrg.class, filter);
	}
	

}
