package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class DivisionUserListLoader extends ListLoader<User> {

	private Object division;

	public DivisionUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		UserQueryHelper.applyFullyActiveFilter(builder);
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.SYSTEM));
		builder.addSimpleWhere("owner", division);
		builder.addOrder("firstName", "lastName");
		
		List<User> users = builder.getResultList(em);
		return users;
	}

	public DivisionUserListLoader setDivision(Object division) {
		this.division = division;
		return this;
	}

}
