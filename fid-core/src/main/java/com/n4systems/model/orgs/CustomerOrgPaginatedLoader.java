package com.n4systems.model.orgs;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CustomerOrgPaginatedLoader extends PaginatedLoader<CustomerOrg> {

	private String nameFilter;
	private boolean withLinkedCustomers = true;
	private boolean archivedOnly;
	
	public CustomerOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<CustomerOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, new TenantOnlySecurityFilter(filter).setShowArchived(archivedOnly));
		builder.addOrder("name");
		
		if (nameFilter != null) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH);
		}
		
		if (!withLinkedCustomers) {
			builder.addWhere(new WhereParameter<Object>(Comparator.NULL, "linkedOrg"));
		}
		
		if (archivedOnly) {
			builder.addWhere(Comparator.EQ, "state", "state", EntityState.ARCHIVED);
		}
		
		return builder;
	}

	public CustomerOrgPaginatedLoader setNameFilter(String nameCodeFilter) {
		this.nameFilter = nameCodeFilter;
		return this;
	}
	
	public CustomerOrgPaginatedLoader setWithLinkedCustomers(boolean withLinkedCustomers) {
		this.withLinkedCustomers = withLinkedCustomers;
		return this;
	}

	public void setArchivedOnly(boolean archivedOnly) {
		this.archivedOnly = archivedOnly;
	}

	public boolean isArchivedOnly() {
		return archivedOnly;
	}
}
