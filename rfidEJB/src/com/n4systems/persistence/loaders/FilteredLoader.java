package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.util.SecurityFilter;

public abstract class FilteredLoader<T> extends Loader<T> {
	private final SecurityFilter filter;
	
	public FilteredLoader(SecurityFilter filter) {
		this.filter = filter;
	}

	abstract protected T load(EntityManager em, SecurityFilter filter);
	
	@Override
	protected T load(EntityManager em) {
		return load(em, filter);
	}

}
