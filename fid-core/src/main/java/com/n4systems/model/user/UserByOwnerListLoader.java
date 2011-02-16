package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UserByOwnerListLoader extends ListLoader<User> {

	private BaseOrg owner;

	public UserByOwnerListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		UserQueryHelper.applyFullyActiveFilter(builder);
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.SYSTEM));
		builder.addSimpleWhere("owner", owner);
		builder.addOrder("firstName", "lastName");
		
		List<User> users = builder.getResultList(em);
		return users;
	}

	public UserByOwnerListLoader owner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}

}
