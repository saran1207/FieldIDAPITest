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
	private String idFilter;
	private Long orgFilter;
	private boolean withLinkedCustomers = true;
	private boolean archivedOnly;
	private String[] postFetchFields = new String[0];
	
	public CustomerOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<CustomerOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, new TenantOnlySecurityFilter(filter).setShowArchived(archivedOnly));
		builder.addOrder("name");
		
		if (nameFilter!=null && !nameFilter.isEmpty()) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH);
		}

		if (idFilter!=null && !idFilter.isEmpty()) {
			builder.addWhere(Comparator.LIKE, "idFilter", "code", idFilter, WhereParameter.IGNORE_CASE | WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH);
		}
		
		if (orgFilter != null) {
			builder.addSimpleWhere("parent.id", orgFilter);
		}

		if (!withLinkedCustomers) {
			builder.addWhere(new WhereParameter<Object>(Comparator.NULL, "linkedOrg"));
		}
		
		if (archivedOnly) {
			builder.addWhere(Comparator.EQ, "state", "state", EntityState.ARCHIVED);
		}
		
		builder.addPostFetchPaths(postFetchFields);
		
		return builder;
	}

	public CustomerOrgPaginatedLoader setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}

	public CustomerOrgPaginatedLoader setIdFilter(String idFilter) {
		this.idFilter = idFilter;
		return this;
	}
	
	public CustomerOrgPaginatedLoader setOrgFilter(Long orgFilter) {
		this.orgFilter = orgFilter;
		return this;
	}

	public CustomerOrgPaginatedLoader setWithLinkedCustomers(boolean withLinkedCustomers) {
		this.withLinkedCustomers = withLinkedCustomers;
		return this;
	}
	
	public CustomerOrgPaginatedLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}

	public void setArchivedOnly(boolean archivedOnly) {
		this.archivedOnly = archivedOnly;
	}

	public boolean isArchivedOnly() {
		return archivedOnly;
	}
}
