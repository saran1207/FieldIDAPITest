package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class GlobalIdLoader<T extends Exportable> extends SecurityFilteredLoader<T> {
	private Class<T> entityClass;
	private String globalId;
	
	public GlobalIdLoader(SecurityFilter filter, Class<T> entityClass) {
		super(filter);
		this.entityClass = entityClass;
	}

	@Override
	protected T load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<T> builder = new QueryBuilder<T>(entityClass, filter);
		builder.addWhere(WhereClauseFactory.create("globalId", globalId));
		
		T entity = builder.getSingleResult(em);
		return entity;
	}

	public GlobalIdLoader<T> setGlobalId(String globalId) {
		this.globalId = globalId;
		return this;
	}

}
