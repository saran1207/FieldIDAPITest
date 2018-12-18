package com.n4systems.model.security;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.Query;

public class SecondaryOrgOnlySecurityFilterNEW extends AbstractSecurityFilter {
	private final Long secondaryOrgId;
	private final Long tenantId;
	private boolean showArchived = false;

	public SecondaryOrgOnlySecurityFilterNEW(Long secondaryOrgId, Long tenantId) {
		this.secondaryOrgId = secondaryOrgId;
		this.tenantId = tenantId;
	}

	public SecondaryOrgOnlySecurityFilterNEW(SecondaryOrg secondaryOrg) {
		this(secondaryOrg.getId(), secondaryOrg.getTenant().getId());
	}

	public SecondaryOrgOnlySecurityFilterNEW(SecurityFilter filter) {
		this(filter.getOwner().getSecondaryOrg().getId(), filter.getOwner().getTenant().getId());
	}
	
	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered() && !showArchived) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isOwnerFiltered()) {
			addFilterParameter(builder, definer.getOwnerPath(), secondaryOrgId);
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered() && !showArchived) {
			setParameter(query, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isOwnerFiltered()) {
			setParameter(query, definer.getOwnerPath(), secondaryOrgId);
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
		
		if (definer.isOwnerFiltered()) {
			addFilterClause(clauses, definer.getOwnerPath(), alias, !firstArgument);
		}
		
		return clauses.toString();
	}

	public Long getSecondaryOrgId() {
		return secondaryOrgId;
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
	
	public void enableShowArchived(){
		 showArchived = true;
	}
	
	public SecondaryOrgOnlySecurityFilterNEW setShowArchived(boolean showArchived) {
		this.showArchived = showArchived;
		return this;
	}
}
