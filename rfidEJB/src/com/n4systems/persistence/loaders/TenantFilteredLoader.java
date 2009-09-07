package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;

abstract public class TenantFilteredLoader<T> extends Loader<T> {

	private final TenantOnlySecurityFilter filter;
	
	public TenantFilteredLoader(TenantOnlySecurityFilter filter) {
		this.filter = filter;
	}
	
	public TenantFilteredLoader(SecurityFilter filter) {
		this(new TenantOnlySecurityFilter(filter));
	}
	
	public TenantFilteredLoader(Tenant tenant) {
		this(new TenantOnlySecurityFilter(tenant));
	}
	
	public TenantFilteredLoader(Long tenantId) {
		this(new TenantOnlySecurityFilter(tenantId));
	}

	abstract protected T load(EntityManager em, TenantOnlySecurityFilter filter);
	
	@Override
	protected T load(EntityManager em) {
		return load(em, filter);
	}

}
