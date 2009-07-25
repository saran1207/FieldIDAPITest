package com.n4systems.model.user;

import java.util.List;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class AdminUserListLoader extends ListLoader<UserBean> {

	public AdminUserListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public AdminUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<UserBean> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, filter.prepareFor(UserBean.class));
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addSimpleWhere("admin", true);
		builder.addOrder("firstName", "lastName");
		
		List<UserBean> users = pm.findAll(builder);
		return users;
	}

}
