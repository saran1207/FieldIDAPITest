package com.n4systems.services;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

@Transactional
public class TenantSecurityFilterDelegate implements SecurityFilter {

	@Autowired
	private PersistenceService persistenceService;
	
	private final String tenantName;
	private TenantOnlySecurityFilter tenantFilter;
	
	public TenantSecurityFilterDelegate() {
		this(null);
	}
	
	public TenantSecurityFilterDelegate(String tenantName) {
		this.tenantName = tenantName; 
	}
	
	private TenantOnlySecurityFilter findOrCreateTenantFilter() {
		if (tenantFilter == null) {
			QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
			builder.addSimpleWhere("name", tenantName);
			builder.addSimpleWhere("disabled", false);
			
			Tenant tenant = persistenceService.find(builder);
			if (tenant == null) {
				throw new SecurityException("Could not find tenant named [" + tenantName + "]");
			}
			
			tenantFilter = new TenantOnlySecurityFilter(tenant);
		}
		return tenantFilter;
	}

	@Override
	public void applyFilter(QueryBuilder<?> builder) {
		findOrCreateTenantFilter().applyFilter(builder);
	}

	@Override
	public void applyParameters(Query query, Class<?> queryClass) {
		findOrCreateTenantFilter().applyParameters(query, queryClass);
	}

	@Override
	public String produceWhereClause(Class<?> queryClass) {
		return findOrCreateTenantFilter().produceWhereClause(queryClass);
	}

	@Override
	public String produceWhereClause(Class<?> queryClass, String tableAlias) {
		return findOrCreateTenantFilter().produceWhereClause(queryClass, tableAlias);
	}

	@Override
	public Long getTenantId() {
		return findOrCreateTenantFilter().getTenantId();
	}

	@Override
	public BaseOrg getOwner() {
		return findOrCreateTenantFilter().getOwner();
	}

	@Override
	public Long getUserId() {
		return findOrCreateTenantFilter().getUserId();
	}

	@Override
	public boolean hasOwner() {
		return findOrCreateTenantFilter().hasOwner();
	}
}
