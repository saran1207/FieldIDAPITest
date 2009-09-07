package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter;

public class ManualSecurityFilter implements QueryFilter {
	private static final String tenantParamName = "securityTenantID";
	private static final String ownerParamName = "securityOwnerID";
	private static final String entityStateParamName = "securityEntityState";
	private static final String userParamName = "securityUserId";
	
	private final Long tenantId;
	private final BaseOrg owner;
	private final Long userId;
	
	private String tenantTarget;
	private String ownerTarget;
	private String entityStateTarget;
	private String userTarget;
	
	public ManualSecurityFilter(SecurityFilter userFilter) {
		this(userFilter.getTenantId(), userFilter.getOwner(), userFilter.getUserId());
	}
	
	public ManualSecurityFilter(Long tenantId, BaseOrg owner, Long userId) {
		this.tenantId = tenantId;
		this.owner = owner;
		this.userId = userId;
	}

	public ManualSecurityFilter setTargets(String tenantTarget, String ownerTarget, String userTarget, String stateTarget) {
		this.tenantTarget = tenantTarget;
		this.ownerTarget = ownerTarget;
		this.userTarget = userTarget;
		this.entityStateTarget = stateTarget;
		return this;
	}
	
	public String produceWhereClause(String tenantTarget, String ownerTarget, String userTarget, String stateTarget) {
		setTargets(tenantTarget, ownerTarget, userTarget, stateTarget);
		return produceWhereClause();
	}
	
	public String produceWhereClause() {
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append(" " + tenantTarget + " = :" + tenantParamName);
		
		if(applyEntityStateFilter()) {
			whereClause.append(" AND " + entityStateTarget + " = :" + entityStateParamName);
		}
		
		if(applyOwnerFilter()) {
			whereClause.append(" AND " + ownerTarget + owner.getFilterPath() + " = :" + ownerParamName);
		}
		
		if (applyUserFilter()) {
			whereClause.append(" AND " + userTarget + " = :" + userParamName);
		}
		
		return whereClause.toString();
	}
	
	public void applyParameters(Query query) {
		query.setParameter(tenantParamName, tenantId );
		
		if(applyEntityStateFilter()) {
			query.setParameter(entityStateParamName, EntityState.ACTIVE);
		}
		
		if(applyOwnerFilter()) {
			query.setParameter(ownerParamName, owner.getId());
		}
		
		if (applyUserFilter()) {
			query.setParameter(userParamName, userId);
		}
	}

	public void applyFilter(QueryBuilder<?> builder) {
		builder.addWhere(getTenantWhereParameter());
		
		if(applyEntityStateFilter()) {
			builder.addWhere(getStateWhereParameter());
		}
		
		if(applyOwnerFilter()) {
			builder.addWhere(getOwnerWhereParameter());
		}
		
		if (applyUserFilter()) {
			builder.addWhere(getUserWhereParameter());
		}
	}
	
	public boolean applyOwnerFilter() {
		return (isOwnerSet() && ownerTarget != null && !owner.isPrimary());
	}
	
	public boolean isOwnerSet() {
		return owner != null;
	}
	
	public boolean applyEntityStateFilter() {
		return (entityStateTarget != null);
	}
	
	public boolean isUserSet() {
		return userId != null;
	}
	
	public boolean applyUserFilter() {
		return (isUserSet() && userTarget != null );
	}
	
	private WhereParameter<Long> getTenantWhereParameter() {
		return new WhereParameter<Long>(WhereParameter.Comparator.EQ, tenantParamName, tenantTarget, tenantId, null, false);
	}
	
	private WhereParameter<Long> getOwnerWhereParameter() {
		return new WhereParameter<Long>(WhereParameter.Comparator.EQ, ownerParamName, ownerTarget + owner.getFilterPath(), owner.getId(), null, false);
	}
	
	private WhereParameter<EntityState> getStateWhereParameter() {
		return new WhereParameter<EntityState>(WhereParameter.Comparator.EQ, entityStateParamName, entityStateTarget, EntityState.ACTIVE, null, false);
	}
	
	private WhereParameter<Long> getUserWhereParameter() {
		return new WhereParameter<Long>(WhereParameter.Comparator.EQ, userParamName, userTarget, userId, null, false);
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public BaseOrg getOwner() {
		return owner;
	}
	
	public Long getUserId() {
		return userId;
	}

}
