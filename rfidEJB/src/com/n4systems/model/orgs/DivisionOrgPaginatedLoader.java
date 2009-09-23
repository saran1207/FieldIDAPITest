package com.n4systems.model.orgs;

import com.n4systems.model.security.OwnerFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class DivisionOrgPaginatedLoader extends PaginatedLoader<DivisionOrg> {
	private CustomerOrg customerFilter;
	private boolean withLinkedDivisions = true;
	
	public DivisionOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected QueryBuilder<DivisionOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<DivisionOrg> builder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, filter);
		builder.addOrder("name");
		
		if (customerFilter != null) {
			builder.applyFilter(new OwnerFilter(customerFilter));
		}
		
		if (!withLinkedDivisions) {
			builder.addWhere(new WhereParameter<Object>(Comparator.NULL, "linkedOrg"));
		}
		
		return builder;
	}
	
	public DivisionOrgPaginatedLoader setCustomerFilter(CustomerOrg customerFilter) {
		this.customerFilter = customerFilter;
		return this;
	}
	
	public DivisionOrgPaginatedLoader setWithLinkedDivisions(boolean withLinkedDivisions) {
		this.withLinkedDivisions = withLinkedDivisions;
		return this;
	}
}
