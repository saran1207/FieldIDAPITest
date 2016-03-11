package com.n4systems.persistence.loaders;

import com.n4systems.model.security.SecurityFilter;

import javax.persistence.EntityManager;


abstract public class SecurityFilteredLoader<T> extends Loader<T> {
	private final SecurityFilter filter;
	
	public SecurityFilteredLoader(SecurityFilter filter) {
		this.filter = filter;
	}

	abstract protected T load(EntityManager em, SecurityFilter filter);
	
	@Override
	public T load(EntityManager em) {
		return load(em, filter);
	}
	
	public Long getTenantId() { 
		return filter.getTenantId();
	}
}
