package com.n4systems.model.user;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredLoader
public class UserFilteredLoader extends SecuredLoader<UserBean> {
	private Long id;
	
	public UserFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public UserFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected UserBean load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, filter.prepareFor(UserBean.class));
		
		builder.addSimpleWhere("uniqueID", id);
		
		UserBean user = pm.find(builder);
		
	    return user;
	}

	public UserFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
