package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CustomerOrgWithArchivedPaginatedLoader extends PaginatedLoader<CustomerOrg> {

	private boolean archivedState;
	private TenantOnlySecurityFilter archivedFilter;

	public CustomerOrgWithArchivedPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<CustomerOrg> createBuilder(SecurityFilter filter) {
		
		archivedFilter = new TenantOnlySecurityFilter(filter);
		if(archivedState){
			archivedFilter.enableShowArchived();
		}
		
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, archivedFilter);
		
		return builder;
	}

	public CustomerOrgWithArchivedPaginatedLoader withArchivedState() {
		archivedState = true;
		return this;
	}

}
