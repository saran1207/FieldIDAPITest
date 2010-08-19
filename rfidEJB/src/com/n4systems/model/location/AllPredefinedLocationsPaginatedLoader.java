package com.n4systems.model.location;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AllPredefinedLocationsPaginatedLoader extends PaginatedLoader<PredefinedLocation> {
	private boolean fetchSearchIds = false;;
	private boolean archivedState = false;
	private TenantOnlySecurityFilter archivedFilter;
	public AllPredefinedLocationsPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<PredefinedLocation> createBuilder(SecurityFilter filter) {
		
		archivedFilter = new TenantOnlySecurityFilter(filter);
		if (archivedState) {
			archivedFilter.enableShowArchived();
		}
		
		QueryBuilder<PredefinedLocation> predefinedLocationBuilder = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, archivedFilter);
		
		if (fetchSearchIds) {
			predefinedLocationBuilder.addPostFetchPaths("searchIds");
		}
		
		return predefinedLocationBuilder;
	}
	
	public AllPredefinedLocationsPaginatedLoader withSearchIds() {
		fetchSearchIds = true;
		return this;
	}
	
	public AllPredefinedLocationsPaginatedLoader withArchivedState() {
		archivedState = true;
		return this;
	}
	
}
