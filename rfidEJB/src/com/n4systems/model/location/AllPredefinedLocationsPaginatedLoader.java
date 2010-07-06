package com.n4systems.model.location;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AllPredefinedLocationsPaginatedLoader extends PaginatedLoader<PredefinedLocation> {

	public AllPredefinedLocationsPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<PredefinedLocation> createBuilder(SecurityFilter filter) {
		return new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
	}

}
