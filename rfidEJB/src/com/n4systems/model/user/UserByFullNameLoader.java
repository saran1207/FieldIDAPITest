package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;


import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.PassthruWhereClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class UserByFullNameLoader extends ListLoader<User> {
	private String fullName;
	
	public UserByFullNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter, "u");
		builder.addWhere(WhereClauseFactory.create("active", true));
		builder.addWhere(WhereClauseFactory.create("deleted", false));
		
		PassthruWhereClause clause = new PassthruWhereClause("full_name");
		clause.setClause("lower(concat(u.firstName, u.lastName)) = :fullName");
		clause.getParams().put("fullName", StringUtils.stripWhitespace(fullName).toLowerCase());
		
		builder.addWhere(clause);
		
		List<User> users = builder.getResultList(em);
		return users;
	}

	public UserByFullNameLoader setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}
	
}
