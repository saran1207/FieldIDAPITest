package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AdminUserListLoader extends ListLoader<UserBean> {

	public AdminUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<UserBean> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, filter);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addSimpleWhere("admin", true);
		builder.addOrder("firstName", "lastName");
		
		List<UserBean> users = builder.getResultList(em);
		return users;
	}

}
