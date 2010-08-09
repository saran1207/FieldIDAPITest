package com.n4systems.persistence.loaders;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class FilteredInListLoader<T> extends ListLoader<T> {
	private final Class<T> clazz;
	private List<Long> ids = new ArrayList<Long>();
	private String[] postFetchFields = new String[0];
	
	public FilteredInListLoader(SecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	public FilteredInListLoader<T> setIds(List<Long> ids) {
		this.ids = ids;
		return this;
	}

	public FilteredInListLoader<T> setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	@Override
	public List<T> load(EntityManager em, SecurityFilter filter) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<T>();
		}
		
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, filter);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "id", ids));
		builder.addPostFetchPaths(postFetchFields);
		
		List<T> entities = builder.getResultList(em);
		return entities;
	}
}
