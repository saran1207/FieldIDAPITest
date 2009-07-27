package com.n4systems.model.user;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class UserUnfilteredLoader extends Loader<UserBean> {
	private Long id;
	
	public UserUnfilteredLoader() {}

	@Override
	protected UserBean load(EntityManager em) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class);
		builder.addSimpleWhere("uniqueID", id);
		
		UserBean user = builder.getSingleResult(em);
		return user;
	}

	public UserUnfilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
