package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;


public class OpenSecurityFilter implements SecurityFilter {

	public OpenSecurityFilter() {}

	public void applyFilter(QueryBuilder<?> builder) {
	}

	public void applyParameters(Query query, Class<?> queryClass) {
	}

	public String produceWhereClause(Class<?> queryClass) {
		return "";
	}

	public String produceWhereClause(Class<?> queryClass, String tableAlias) {
		return "";
	}

	public Long getTenantId() {
		return null;
	}

	public BaseOrg getOwner() {
		return null;
	}

	public Long getUserId() {
		return null;
	}

	public boolean hasOwner() {
		return false;
	}
}
