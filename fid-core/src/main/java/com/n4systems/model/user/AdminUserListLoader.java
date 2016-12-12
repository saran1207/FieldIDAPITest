package com.n4systems.model.user;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class AdminUserListLoader extends ListLoader<User> {

	public AdminUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		UserQueryHelper.applyFullyActiveFilter(builder);
		builder.addSimpleWhere("userType", UserType.ADMIN);
		builder.addOrder("firstName", "lastName");
		
		List<User> users = builder.getResultList(em);
		return users;
	}

}
