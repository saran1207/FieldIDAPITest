package com.n4systems.persistence.loaders;

import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class ListableLoader extends ListLoader<Listable<Long>> {
	
	public ListableLoader(SecurityFilter filter) {
		super(filter);
	}

	protected abstract QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter);

	@Override
	protected List<Listable<Long>> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = createBuilder(filter);
		
		List<Listable<Long>> listables = builder.getResultList(em);
		
		return listables;
	}
}
