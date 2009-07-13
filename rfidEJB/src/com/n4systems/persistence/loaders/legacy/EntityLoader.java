package com.n4systems.persistence.loaders.legacy;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.ServiceLocator;

/**
 * @deprecated Use {@link Loader} instead
 */
@Deprecated
abstract public class EntityLoader<T> {
	
	private final PersistenceManager pm;
	
	public EntityLoader() {
		this(null);
	}

	public EntityLoader(PersistenceManager pm) {
		if (pm == null) {
			pm = ServiceLocator.getPersistenceManager();
		}
		
		this.pm = pm;
	}
	
	abstract protected T load(PersistenceManager pm);
	
	public T load() {
		return load(pm);
	}
	
}
