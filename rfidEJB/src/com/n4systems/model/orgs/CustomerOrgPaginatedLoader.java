package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CustomerOrgPaginatedLoader extends PaginatedLoader<CustomerOrg> {

	private String nameFilter;
	
	public CustomerOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<CustomerOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
		builder.addOrder("name");
		
		if (nameFilter != null) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH);
		}
		
		return builder;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameCodeFilter) {
		this.nameFilter = nameCodeFilter;
	}
	
}
