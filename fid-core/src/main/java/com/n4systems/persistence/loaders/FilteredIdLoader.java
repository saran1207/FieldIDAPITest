package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FilteredIdLoader<T extends AbstractEntity> extends SecurityFilteredLoader<T> implements IdLoader<FilteredIdLoader<T>> {
	protected final Class<T> clazz;
	private Long id;
	private String[] postFetchFields = new String[0];
	
	public FilteredIdLoader(SecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	public FilteredIdLoader<T> setId(Long id) {
		this.id = id;
		return this;
	}

	public FilteredIdLoader<T> setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	@Override
	public T load(EntityManager em, SecurityFilter filter) {
		guard();
		
		QueryBuilder<T> builder = createQueryBuilder(filter);
		builder.addSimpleWhere("id", id);
		builder.addPostFetchPaths(postFetchFields);
		
		T entity = builder.getSingleResult(em);
		return entity;
	}

	protected QueryBuilder<T> createQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<T>(clazz, filter);
	}
	
	private void guard() {
		if (id == null) {
			throw new InvalidArgumentException("you must specify an id to load.");
		}
	}	 
}
