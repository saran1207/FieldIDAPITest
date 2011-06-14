package com.n4systems.model.user;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class UserFilteredLoader extends SecurityFilteredLoader<User> {
	private Long id;

	public UserFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected User load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addSimpleWhere("id", id);
		
		User user = builder.getSingleResult(em);
	    return user;
	}

	public UserFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
