package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FilteredIdLoader<T extends AbstractEntity> extends SecurityFilteredLoader<T> {
	private final Class<T> clazz;
	private Long id;
	
	public FilteredIdLoader(SecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	public FilteredIdLoader<T> setId(Long id) {
		this.id = id;
		return this;
	}

	@Override
	public T load(EntityManager em, SecurityFilter filter) {
		gaurd();
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, filter);
		builder.addSimpleWhere("id", id);
		T entity = builder.getSingleResult(em);
		return entity;
	}
	
	private void gaurd() {
		if (id == null) {
			throw new InvalidArgumentException("you must specify an id to load.");
		}
	}
	
}
