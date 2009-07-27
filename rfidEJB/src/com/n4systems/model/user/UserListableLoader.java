package com.n4systems.model.user;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UserListableLoader extends ListableLoader {
	private boolean noCustomer = true;
	private boolean noDeleted = false;
	private boolean activeOnly = true;

	public UserListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		/*
		 * We're currently only filtering by tenant, this comes from the original UserManager method.  Not sure if it should be changed.
		 */
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(UserBean.class, filter.newFilter().setTargets("tenant.id"));
		builder.setSelectArgument(new ListableSelect("uniqueID", "CONCAT(firstName, ' ', lastName)"));
		builder.addSimpleWhere("system", false);
		builder.addOrder("firstName", "lastName");
		
		if (activeOnly) {
			builder.addSimpleWhere("active", true);
		}
		
		if (noCustomer) {
			builder.addWhere(new WhereParameter<Long>(Comparator.NULL, "r_EndUser"));
		}
		
		
		if (noDeleted) {
			builder.addSimpleWhere("deleted", false);
		}
		
		return builder;
	}

	public void setNoCustomer(boolean noCustomer) {
		this.noCustomer = noCustomer;
	}

	public void setNoDeleted(boolean noDeleted) {
		this.noDeleted = noDeleted;
	}

}
