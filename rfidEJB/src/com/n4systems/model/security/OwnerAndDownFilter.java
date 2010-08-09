package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.persistence.QueryBuilder;

public class OwnerAndDownFilter extends AbstractSecurityFilter {

	private final BaseOrg filterOrg;
	
	public OwnerAndDownFilter(BaseOrg filterOrg) {
		this.filterOrg = filterOrg;
	}

	@Override
	protected String getFieldPrefix() {
		return "owner_filter_";
	}

	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (!definer.isOwnerFiltered()) {
			throw new SecurityException("OwnerFilter can only be used on entities with an owner");
		}
		if (filterOrg == null) {
			return;
		}
		
		if (filterOrg.isPrimary()) {
			addNullFilterParameter(builder, prepareFullOwnerPathWithFilterPath(definer, SecondaryOrg.SECONDARY_ID_FILTER_PATH));
		} else {
			addFilterParameter(builder, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		throw new SecurityException("Not Implemented");
	}

	@Override
	protected String produceWhereClause(String alias, SecurityDefiner definer) throws SecurityException {
		throw new SecurityException("Not Implemented");
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
