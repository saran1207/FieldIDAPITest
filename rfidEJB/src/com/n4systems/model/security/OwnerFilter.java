package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;

public class OwnerFilter extends AbstractSecurityFilter {

	private final BaseOrg filterOrg;
	
	public OwnerFilter(BaseOrg filterOrg) {
		this.filterOrg = filterOrg;
	}

	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (!definer.isOwnerFiltered()) {
			throw new SecurityException("OwnerFilter can only be used on entities with an owner");
		}
		
		if (filterOrg.isPrimary()) {
			// if the org is Primary we'll add a filter for the Tenant rather then the PriaryOrg since their 1-to-1
			addFilterParameter(builder, definer.getTenantPath(), getTenantId());
		} else {
			addFilterParameter(builder, definer.getOwnerPath() + filterOrg.getFilterPath(), filterOrg.getId());
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (!definer.isOwnerFiltered()) {
			throw new SecurityException("OwnerFilter can only be used on entities with an owner");
		}
		
		if (filterOrg.isPrimary()) {
			// if the org is Primary we'll add a filter for the Tenant rather then the PriaryOrg since their 1-to-1
			setParameter(query, definer.getTenantPath(), getTenantId());
		} else {
			setParameter(query, definer.getOwnerPath(), filterOrg.getId());
		}
	}

	@Override
	protected String produceWhereClause(String alias, SecurityDefiner definer) throws SecurityException {
		StringBuilder clauses = new StringBuilder();
		
		if (!definer.isOwnerFiltered()) {
			throw new SecurityException("OwnerFilter can only be used on entities with an owner");
		}
		
		if (filterOrg.isPrimary()) {
			// if the org is Primary we'll add a filter for the Tenant rather then the PriaryOrg since their 1-to-1
			addFilterClause(clauses, definer.getTenantPath(), alias, false);
		} else {
			addFilterClause(clauses, definer.getTenantPath(), alias, false);
		}
		
		return clauses.toString();
	}

	public Long getTenantId() {
		return filterOrg.getTenant().getId();
	}

	public BaseOrg getOwner() {
		return filterOrg;
	}

	public Long getUserId() {
		return null;
	}

	public boolean hasOwner() {
		return filterOrg != null;
	}
}
