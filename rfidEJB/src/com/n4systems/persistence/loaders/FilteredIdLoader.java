package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FilteredIdLoader<T extends AbstractEntity & FilteredEntity> extends FilteredLoader<T> {

	private final Class<T> clazz;
	private Long id;
	
	public FilteredIdLoader(SecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public T load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, filter.prepareFor(clazz));
		builder.addSimpleWhere("id", id);
		T entity = builder.getSingleResult(em);
		return entity;
	}
	
}
