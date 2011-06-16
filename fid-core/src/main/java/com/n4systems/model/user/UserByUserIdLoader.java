package com.n4systems.model.user;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

public class UserByUserIdLoader extends SecurityFilteredLoader<User> {

	private String userID;
	
	public UserByUserIdLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected User load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addWhere(WhereClauseFactory.create("userID", userID, WhereParameter.IGNORE_CASE, ChainOp.AND));		
		return builder.getSingleResult(em);
	}
	
	public UserByUserIdLoader setUserID(String userID) {
		this.userID = StringUtils.clean(userID);
        return this;
	}


}
