package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;


abstract public class SecurityFilteredLoader<T> extends Loader<T> {
	private final SecurityFilter filter;
	
	public SecurityFilteredLoader(SecurityFilter filter) {
		this.filter = filter;
	}

	abstract protected T load(EntityManager em, SecurityFilter filter);
	
	@Override
	protected T load(EntityManager em) {
		return load(em, filter);
	}
	
	public Long getTenantId() { 
		return filter.getTenantId();
	}
}
