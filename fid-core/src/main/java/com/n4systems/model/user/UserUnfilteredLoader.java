package com.n4systems.model.user;

import javax.persistence.EntityManager;


import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class UserUnfilteredLoader extends Loader<User> {
	private Long id;
	
	public UserUnfilteredLoader() {}

	@Override
	protected User load(EntityManager em) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		builder.addSimpleWhere("id", id);
		
		User user = builder.getSingleResult(em);
		return user;
	}

	public UserUnfilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
