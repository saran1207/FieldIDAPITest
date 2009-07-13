package com.n4systems.persistence.loaders.legacy;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.api.Listable;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated Use {@link com.n4systems.persistence.loaders.ListableLoader} instead
 */
@Deprecated
public abstract class ListableLoader extends ListLoader<Listable<Long>> {
	
	public ListableLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ListableLoader(SecurityFilter filter) {
		super(filter);
	}

	protected abstract QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter);

	@Override
	protected List<Listable<Long>> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = createBuilder(filter);
		
		List<Listable<Long>> listables = pm.findAll(builder);
		
		return listables;
	}
}
