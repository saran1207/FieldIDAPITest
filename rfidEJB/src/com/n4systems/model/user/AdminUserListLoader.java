package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;


import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AdminUserListLoader extends ListLoader<User> {

	public AdminUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addSimpleWhere("admin", true);
		builder.addOrder("firstName", "lastName");
		
		List<User> users = builder.getResultList(em);
		return users;
	}

}
