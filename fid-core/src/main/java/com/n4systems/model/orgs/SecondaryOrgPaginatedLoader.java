package com.n4systems.model.orgs;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SecondaryOrgPaginatedLoader extends PaginatedLoader<SecondaryOrg> {
	
	private EntityState state = EntityState.ACTIVE;
	private boolean archivedState;
	private TenantOnlySecurityFilter archivedFilter;

	public SecondaryOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<SecondaryOrg> createBuilder(SecurityFilter filter) {
		
		archivedFilter = new TenantOnlySecurityFilter(filter);
		if(archivedState){
			archivedFilter.enableShowArchived();
		}
		
		QueryBuilder<SecondaryOrg> builder = new QueryBuilder<SecondaryOrg>(SecondaryOrg.class, archivedFilter);
		builder.addSimpleWhere("state", state);
		builder.addOrder("name");
		return builder;
	}
	
	public SecondaryOrgPaginatedLoader withArchivedState(){
		archivedState = true;
		state = EntityState.ARCHIVED;
		return this;
	}

}
