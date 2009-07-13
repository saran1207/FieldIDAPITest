package com.n4systems.persistence.loaders.legacy;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.util.SecurityFilter;

/**
 * @deprecated Use {@link FilteredLoader} instead
 */
@Deprecated
abstract public class SecuredLoader<T> extends EntityLoader<T> {
	private final SecurityFilter filter;
	
	public SecuredLoader(SecurityFilter filter) {
		this(null, filter);
	}

	public SecuredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm);
		this.filter = filter;
	}

	abstract protected T load(PersistenceManager pm, SecurityFilter filter);
	
	@Override
	protected T load(PersistenceManager pm) {
		return load(pm, filter);
	}
	
}
