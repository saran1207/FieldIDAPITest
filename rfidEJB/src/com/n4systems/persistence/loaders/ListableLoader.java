package com.n4systems.persistence.loaders;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.api.Listable;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

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
