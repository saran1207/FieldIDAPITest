package com.n4systems.model.user;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class UserFilteredLoader extends SecurityFilteredLoader<UserBean> {
	private Long id;

	public UserFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected UserBean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, filter);
		builder.addSimpleWhere("uniqueID", id);
		
		UserBean user = builder.getSingleResult(em);
	    return user;
	}

	public UserFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
