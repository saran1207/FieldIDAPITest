package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantOnlySecurityFilter extends AbstractSecurityFilter {
	private final Long tenantId;
	
	public TenantOnlySecurityFilter(Long tenantId) {
		this.tenantId = tenantId;
	}
	
	public TenantOnlySecurityFilter(Tenant tenant) {
		this(tenant.getId());
	}
	
	public TenantOnlySecurityFilter(SecurityFilter filter) {
		this(filter.getTenantId());
	}
	
	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			addFilterParameter(builder, definer.getTenantPath(), tenantId);			
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			setParameter(query, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			setParameter(query, definer.getTenantPath(), tenantId);
		}
	}

	@Override
	protected String produceWhereClause(String alias, SecurityDefiner definer) throws SecurityException {
		StringBuilder clauses = new StringBuilder();
		boolean firstArgument = true;
		
		if (definer.isStateFiltered()) {
			addFilterClause(clauses, definer.getStatePath(), alias, false);
			firstArgument = false;
		}
		
		if (definer.isTenantFiltered()) {
			addFilterClause(clauses, definer.getTenantPath(), alias, !firstArgument);
		}
		
		return clauses.toString();
	}

	public Long getTenantId() {
		return tenantId;
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
