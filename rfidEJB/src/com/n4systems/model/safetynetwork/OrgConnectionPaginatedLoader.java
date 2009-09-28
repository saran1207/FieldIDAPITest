package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class OrgConnectionPaginatedLoader extends PaginatedLoader<OrgConnection> {
	private final OrgConnectionType connectionListType;

	public OrgConnectionPaginatedLoader(SecurityFilter filter, OrgConnectionType type) {
		super(filter);
		connectionListType = type;
	}
	
	@Override
	protected QueryBuilder<OrgConnection> createBuilder(SecurityFilter filter) {
		return OrgConnectionQueryBuilderFactory.createListQuery(filter, connectionListType);
	}

}
