package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class BaseOrgListLoaderTestExtention extends BaseOrgListLoader {


	private QueryBuilder<BaseOrg> queryBuilder;
	
	public BaseOrgListLoaderTestExtention(SecurityFilter filter) {
		super(filter);
	}
	
	
	
	public void setQueryBuild(QueryBuilder<BaseOrg> builder) {
		this.queryBuilder = builder;
	}



	@Override
	protected QueryBuilder<BaseOrg> getQueryBuilder(SecurityFilter filter) {
		return queryBuilder;
	}
}
