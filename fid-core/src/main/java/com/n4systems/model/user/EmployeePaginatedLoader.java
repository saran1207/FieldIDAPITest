package com.n4systems.model.user;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeePaginatedLoader extends PaginatedLoader<User> {

	public EmployeePaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<User> createBuilder(SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("active", true);
		builder.addOrder("id");
		
		builder.addWhere(Comparator.NULL, "customerid", "owner.customerOrg", "");
		
		return builder;
	}
	
	
}
