package com.n4systems.persistence.loaders;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantFilteredListLoader<T> extends ListLoader<T> {
	private final Class<T> clazz;
	
	public TenantFilteredListLoader(Long tenantId, Class<T> clazz) {
		super(new TenantOnlySecurityFilter(tenantId));
		this.clazz = clazz;
	}

	public TenantFilteredListLoader(Tenant tenant, Class<T> clazz) {
		this(tenant.getId(), clazz);
	}

	public TenantFilteredListLoader(TenantOnlySecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	@Override
	protected List<T> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, filter);
		
		List<T> entities = builder.getResultList(em);
		return entities;
	}

}
