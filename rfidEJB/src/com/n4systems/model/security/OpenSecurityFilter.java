package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;


public class OpenSecurityFilter extends AbstractSecurityFilter {

	public OpenSecurityFilter() {}

	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			setParameter(query, definer.getStatePath(), EntityState.ACTIVE);
		}
	}

	@Override
	protected String produceWhereClause(String alias, SecurityDefiner definer) throws SecurityException {
		StringBuilder clauses = new StringBuilder();
		
		if (definer.isStateFiltered()) {
			addFilterClause(clauses, definer.getStatePath(), alias, false);
		}
		
		return clauses.toString();
	}

	public BaseOrg getOwner() {
		return null;
	}

	public Long getTenantId() {
		return null;
	}

	public Long getUserId() {
		return null;
	}

	public boolean hasOwner() {
		return false;
	}


}
