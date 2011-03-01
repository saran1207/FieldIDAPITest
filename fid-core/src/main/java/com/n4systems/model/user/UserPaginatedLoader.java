package com.n4systems.model.user;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UserPaginatedLoader extends PaginatedLoader<User> {

	private BaseOrg owner;
	private UserType userType;
	private String nameFilter;
	private boolean archivedOnly = false;
	
	public UserPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<User> createBuilder(SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.SYSTEM));
		UserQueryHelper.applyRegisteredFilter(builder);		
		
		if(owner != null) {
			builder.addSimpleWhere("owner", owner);
		}
				
		if (userType != null && !userType.equals(UserType.ALL)) {
			builder.addSimpleWhere("userType", userType);
		}
		
		if (nameFilter != null && !nameFilter.isEmpty()) {	
			WhereParameterGroup whereGroup = new WhereParameterGroup();
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "userID", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH, ChainOp.OR));
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "firstName", nameFilter, WhereParameter.IGNORE_CASE| WhereParameter.WILDCARD_BOTH, ChainOp.OR));
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "lastName", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH, ChainOp.OR));
			builder.addWhere(whereGroup);
		}
		
		if (archivedOnly) {
			UserQueryHelper.applyArchivedFilter(builder);			
		}
		
		builder.addOrder("firstName", "lastName");

		return builder;
	}
	
	public UserPaginatedLoader withOwner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}

	public UserPaginatedLoader withUserType(UserType userType) {
		this.userType = userType;
		return this;
	}

	public UserPaginatedLoader withNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}
	
	public UserPaginatedLoader withArchivedOnly() {
		this.archivedOnly = true;
		return this;
	}

}
