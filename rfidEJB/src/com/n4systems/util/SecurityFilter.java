package com.n4systems.util;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.model.security.SecurityFilterFactory;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

import rfid.ejb.entity.UserBean;

import java.io.Serializable;

import javax.persistence.Query;

public class SecurityFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String tenantParamName = "securityTenantID";
	private static final String customerParamName = "securityCustomerID";
	private static final String divisionParamName = "securityDivisionID";
	private static final String entityStateParamName = "securityEntityState";
	private static final String userParamName = "securityUserId";
	
	private Long tenantId;
	private Long customerId;
	private Long divisionId;
	private Long userId;
	
	private String tenantTarget;
	private String customerTarget;
	private String divisionTarget;
	private String entityStateTarget;
	private String userTarget;
	
	public SecurityFilter(Long tenantId) {
		this(tenantId, null, null, null);
	}

	public SecurityFilter(Long tenantId, Long customerId) {
		this(tenantId, customerId, null, null);
	}
	
	public SecurityFilter(Long tenantId, Long customerId, Long divisionId) {
		this(tenantId, customerId, divisionId, null);
	}
	
	public SecurityFilter(Long tenantId, Long customerId, Long divisionId, Long userId) {
		this.tenantId = tenantId;
		this.customerId = customerId;
		this.divisionId = divisionId;
		this.userId = userId;
	}
	
	public SecurityFilter(UserBean user) {
		this(user.getTenant().getId(), user.getCustomerId(), user.getDivisionId(), user.getUniqueID());
	}
	
	public String produceWhereClause(String tenantTarget, String customerTarget, String divisionTarget) {
		setTargets(tenantTarget, customerTarget, divisionTarget);
		return produceWhereClause();
	}
	
	public String produceWhereClause() {
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append(" " + tenantTarget + " = :" + tenantParamName);
		
		if(applyEntityStateFilter()) {
			whereClause.append(" AND " + entityStateTarget + " = :" + entityStateParamName);
		}
		
		if(applyCustomerFilter()) {
			whereClause.append(" AND " + customerTarget + " = :" + customerParamName);
			
			if(applyDivisionFilter()) {
				whereClause.append(" AND " + divisionTarget + " = :" + divisionParamName);
			}
		}
		
		if (applyUserFilter()) {
			whereClause.append(" AND " + userTarget + " = :" + userParamName);
		}
		
		return whereClause.toString();
	}
	
	public SecurityFilter setTargets(String tenantTarget) {
		return setTargets(tenantTarget, null, null, null, null);
	}
	
	public SecurityFilter setTargets(String tenantTarget, String customerTarget) {
		return setTargets(tenantTarget, customerTarget, null, null, null);
	}
	
	public SecurityFilter setTargets(String tenantTarget, String customerTarget, String divisionTarget) {
		return setTargets(tenantTarget, customerTarget, divisionTarget, null, null);
	}
	
	public SecurityFilter setTargets(String tenantTarget, String customerTarget, String divisionTarget, String userTarget, String stateTarget) {
		this.tenantTarget = tenantTarget;
		this.customerTarget = customerTarget;
		this.divisionTarget = divisionTarget;
		this.userTarget = userTarget;
		this.entityStateTarget = stateTarget;
		return this;
	}
	
	public SecurityFilter setDefaultTargets() {
		return setTargets("tenant.id", "customer.id", "division.id", null, null);
	}
	
	public void applyParamers(Query query) {
		query.setParameter(tenantParamName, tenantId );
		
		if(applyEntityStateFilter()) {
			query.setParameter(entityStateParamName, EntityState.ACTIVE);
		}
		
		if(applyCustomerFilter()) {
			query.setParameter(customerParamName, customerId);
			
			if(applyDivisionFilter()) {
				query.setParameter(divisionParamName, divisionId);
			}
		}
		
		if (applyUserFilter()) {
			query.setParameter(userParamName, userId);
		}
	}
	
	public void applyParamers(QueryBuilder<?> builder) {
		builder.addWhere(getTenantWhereParameter());
		
		if(applyEntityStateFilter()) {
			builder.addWhere(getStateWhereParameter());
		}
		
		if(applyCustomerFilter()) {
			builder.addWhere(getCustomerWhereParameter());
			
			if(applyDivisionFilter()) {
				builder.addWhere(getDivisionWhereParameter());
			}
		}
		
		if (applyUserFilter()) {
			builder.addWhere(getUserWhereParameter());
		}
	}
	
	public boolean applyCustomerFilter() {
		return ( isCustomerSet() && customerTarget != null );
	}
	
	public boolean isCustomerSet() {
		return !( customerId == null || customerId < 1L );
	}
		
	public boolean applyDivisionFilter() {
		return ( isDivisionSet() && divisionTarget != null );
	}
	
	public boolean isDivisionSet() {
		return !( divisionId == null || divisionId < 1L );
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
	
	private WhereParameter<Long> getCustomerWhereParameter() {
		return new WhereParameter<Long>(WhereParameter.Comparator.EQ, customerParamName, customerTarget, customerId, null, false);
	}
	
	private WhereParameter<Long> getDivisionWhereParameter() {
		return new WhereParameter<Long>(WhereParameter.Comparator.EQ, divisionParamName, divisionTarget, divisionId, null, false);
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

	public Long getCustomerId() {
		return customerId;
	}

	public Long getDivisionId() {
		return divisionId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public SecurityFilter newFilter() {
		return new SecurityFilter(tenantId, customerId, divisionId, userId);
	}
	
	public SecurityFilter prepareFor(Class<? extends FilteredEntity> clazz) {
		return SecurityFilterFactory.prepare(clazz, this);
	}
}
