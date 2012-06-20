package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.Query;

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

        builder.addWhere(createAppropriateParameter(definer));
	}

    public WhereParameter<?> createAppropriateParameter(SecurityDefiner definer) {
        if (filterOrg.isPrimary()) {
            return createFilterParameter(prepareFullOwnerPathWithFilterPath(definer, SecondaryOrg.SECONDARY_ID_FILTER_PATH), null, WhereParameter.Comparator.EQ_OR_NULL);
        } else {
            return createFilterParameter(prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
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
