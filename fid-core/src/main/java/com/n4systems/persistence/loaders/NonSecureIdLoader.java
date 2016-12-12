package com.n4systems.persistence.loaders;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class NonSecureIdLoader<T extends BaseEntity> extends Loader<T> implements IdLoader<NonSecureIdLoader<T>> {

	private final Class<T> clazz;
	private Long id;
	private String[] postFetchPaths;
	
	public NonSecureIdLoader(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T load(EntityManager em) {
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, new OpenSecurityFilter());
		builder.addSimpleWhere("id", id);
		
		if (postFetchPaths != null) {
			builder.addPostFetchPaths(postFetchPaths);
		}
		
		return builder.getSingleResult(em);
	}

	public NonSecureIdLoader<T> setId(Long id) {
		this.id = id;
		return this;
	}
	
	public NonSecureIdLoader<T> setPostFetchPaths(String...paths) {
		this.postFetchPaths = paths;
		return this;
	}
}
