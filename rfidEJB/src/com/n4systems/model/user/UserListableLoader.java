package com.n4systems.model.user;


import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class UserListableLoader extends ListableLoader {
	private boolean noDeleted = false;

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
		
		builder.addSimpleWhere("system", false);
		builder.addOrder("firstName", "lastName");
		builder.addSimpleWhere("active", true);
		
		if (noDeleted) {
			builder.addSimpleWhere("deleted", false);
		}
		
		return builder;
	}


	public UserListableLoader setNoDeleted(boolean noDeleted) {
		this.noDeleted = noDeleted;
		return this;
	}

}
