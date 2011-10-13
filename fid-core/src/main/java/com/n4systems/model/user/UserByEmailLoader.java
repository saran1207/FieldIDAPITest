package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

public class UserByEmailLoader extends ListLoader<User> {

	private String userEmail;
	
	public UserByEmailLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addWhere(WhereClauseFactory.create("emailAddress", userEmail, WhereParameter.IGNORE_CASE | WhereParameter.TRIM , ChainOp.AND));
		
		return builder.getResultList(em);
	}
	
	public UserByEmailLoader setEmail(String userEmail) {
		this.userEmail = (userEmail != null) ? userEmail.toLowerCase() : null;
        return this;
	}

}
