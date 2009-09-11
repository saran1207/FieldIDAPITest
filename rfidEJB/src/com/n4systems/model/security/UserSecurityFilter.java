package com.n4systems.model.security;

import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;

public class UserSecurityFilter extends AbstractSecurityFilter {
	
	private final BaseOrg filterOrg;
	private final Long filterUserId;
	
	public UserSecurityFilter(BaseOrg filterOrg, Long filterUserId) {
		this.filterOrg = filterOrg;
		this.filterUserId = filterUserId;
	}
	
	public UserSecurityFilter(UserBean user) {
		this(user.getOwner(), user.getId());
	}

	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			// The tenant filter is almost always applied along with the user/owner filter as a safety precaution
			addFilterParameter(builder, definer.getTenantPath(), getTenantId());
			
			if (definer.isUserFiltered() && filterUserId != null) {
				// we check to see if this is user filtered first since you can't get more specific then a user filter
				addFilterParameter(builder, definer.getUserPath(), filterUserId);
				
			} else if (definer.isOwnerFiltered() && !filterOrg.isPrimary()) {
				// we don't need to add an owner filter for a PrimaryOrg since it's 1 to 1 with the tenant
				addFilterParameter(builder, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
			}
		}
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			setParameter(query, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		if (definer.isTenantFiltered()) {
			setParameter(query, definer.getTenantPath(), getTenantId());
			
			if (definer.isUserFiltered()) {
				setParameter(query, definer.getUserPath(), filterUserId);
				
			} else if (definer.isOwnerFiltered() && !filterOrg.isPrimary()) {
				setParameter(query, prepareFullOwnerPath(definer, filterOrg), filterOrg.getId());
			}
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
			// The tenant filter is almost always applied along with the user/owner filter as a safety precaution
			// If this is the first argument, do not prepend an AND
			addFilterClause(clauses, definer.getTenantPath(), alias, !firstArgument);
			
			if (definer.isUserFiltered()) {
				// we check to see if this is user filtered first since you can't get more specific then a user filter
				addFilterClause(clauses, definer.getUserPath(), alias, true);
				
			} else if (definer.isOwnerFiltered() && !filterOrg.isPrimary()) {
				// we don't need to add an owner filter for a PrimaryOrg since it's 1 to 1 with the tenant
				addFilterClause(clauses, prepareFullOwnerPath(definer, filterOrg), alias, true);
			}
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
		return filterUserId;
	}

	public boolean hasOwner() {
		return filterOrg != null;
	}
}
