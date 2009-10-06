package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class PaginatedConnectionListLoader extends PaginatedLoader<TypedOrgConnection> {

	
	public PaginatedConnectionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<TypedOrgConnection> createBuilder(SecurityFilter filter) {
		QueryBuilder<TypedOrgConnection> query = new QueryBuilder<TypedOrgConnection>(TypedOrgConnection.class, filter);
		query.addOrder("connectedOrg.name");
		
		return query;
	}

}
