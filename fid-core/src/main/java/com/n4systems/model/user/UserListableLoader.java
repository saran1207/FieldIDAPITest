package com.n4systems.model.user;


import java.util.Arrays;

import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.UserType;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UserListableLoader extends ListableLoader {
	private boolean noDeleted = false;
	private boolean employeesOnly = false;

	public UserListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {

		/*
		 * We're currently only filtering by tenant, this comes from the original UserManager method.  Not sure if it should be changed.
		 */
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(User.class, filter);
		builder.setSelectArgument(new ListableSelect("id", "CONCAT(firstName, ' ', lastName)"));
		
		builder.addOrder("firstName", "lastName");
		builder.addSimpleWhere("active", true);
		
		if (noDeleted) {
			builder.addSimpleWhere("deleted", false);
		}
		
		if (employeesOnly) {
			builder.addWhere(WhereClauseFactory.create(Comparator.IN, "userType", Arrays.asList(UserType.ADMIN, UserType.EMPLOYEES, UserType.LITE)));
		}
		
		return builder;
	}


	public UserListableLoader setNoDeleted(boolean noDeleted) {
		this.noDeleted = noDeleted;
		return this;
	}
	
	public UserListableLoader employeesOnly() {
		employeesOnly = true;
		return this;
	}

}
