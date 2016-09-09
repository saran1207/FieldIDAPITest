package com.n4systems.persistence.loaders;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class AllEntityListLoader<T> extends Loader<List<T>> {
	private final Class<T> clazz;
	
	public AllEntityListLoader(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public List<T> load(EntityManager em) {
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, new OpenSecurityFilter());
		
		List<T> entities = builder.getResultList(em);
		return entities;
	}
}
