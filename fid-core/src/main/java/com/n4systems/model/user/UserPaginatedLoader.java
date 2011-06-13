package com.n4systems.model.user;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.ExternalOrgFilter;
import com.n4systems.model.orgs.InternalOrgFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.security.UserType;
import com.n4systems.util.UserGroup;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.JoinClause.JoinType;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.OrderClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

public class UserPaginatedLoader extends PaginatedLoader<User> {

	private BaseOrg owner;
	private CustomerOrg customer;
	private Long orgFilter;
	private UserType userType;
	private UserGroup userGroup = UserGroup.ALL;
	private String nameFilter;
	private boolean archivedOnly = false;
	private boolean filterOnPrimaryOrg;
	private boolean filterOnSecondaryOrg;
	private String order;
	private boolean ascending;

	private static String [] DEFAULT_ORDER = {"firstName", "lastName"};

	public UserPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<User> createBuilder(SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.SYSTEM));
		UserQueryHelper.applyRegisteredFilter(builder);

		if (owner != null) {
			builder.addSimpleWhere("owner", owner);
		}
		
		if (customer != null) {
			builder.addSimpleWhere("owner.customerOrg", customer);
		}
		
		if(filterOnPrimaryOrg) {
			builder.addWhere(WhereClauseFactory.createIsNull("owner.secondaryOrg"));
		}
					
		if(filterOnSecondaryOrg) {
			builder.addSimpleWhere("owner.secondaryOrg.id", orgFilter);
		}

		if (userType != null && userType != UserType.ALL) {
			builder.addSimpleWhere("userType", userType);
		}
		
		if (userGroup == UserGroup.CUSTOMER) {
			builder.applyFilter(new ExternalOrgFilter("owner"));
		} else if (userGroup == UserGroup.EMPLOYEE) {
			builder.applyFilter(new InternalOrgFilter("owner"));
		}

		if (nameFilter != null && !nameFilter.isEmpty()) {
			WhereParameterGroup whereGroup = new WhereParameterGroup();
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "userID", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
					ChainOp.OR));
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "firstName", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
					ChainOp.OR));
			whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "lastName", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
					ChainOp.OR));
			builder.addWhere(whereGroup);
		}

		if (archivedOnly) {
			UserQueryHelper.applyArchivedFilter(builder);
		}

		if(order != null && !order.isEmpty()) {
			if(order.startsWith("owner")) { 
				builder.addJoin(new JoinClause(JoinType.LEFT, order, "sort", true));
				
		        OrderClause orderClause1 = new OrderClause("sort", ascending);
		        orderClause1.setAlwaysDropAlias(true);
		        		        
				builder.getOrderArguments().add(orderClause1);
				builder.getOrderArguments().add(new OrderClause("id", ascending));
			} else
				builder.setOrder(order, ascending);
		} else {
			for (String order : DEFAULT_ORDER) {
				builder.setOrder(order, ascending);				
			}
		}

		return builder;
	}

	public UserPaginatedLoader withOwner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}
	
	public UserPaginatedLoader withOrgFilter(Long orgFilter) {
		this.orgFilter = orgFilter;
		return this;
	}

	public UserPaginatedLoader filterOnPrimaryOrg() {
		this.filterOnPrimaryOrg = true;
		return this;
	}

	public UserPaginatedLoader filterOnSecondaryOrg() {
		this.filterOnSecondaryOrg = true;
		return this;
	}
	
	public UserPaginatedLoader withUserType(UserType userType) {
		this.userType = userType;
		return this;
	}

	public UserPaginatedLoader withUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
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

	public UserPaginatedLoader withOrder(String order, boolean ascending) {
		this.order = order;
		this.ascending = ascending;
		return this;
	}

	public UserPaginatedLoader withCustomer(CustomerOrg customer) {
		this.customer = customer;
		return this;
	}
}
