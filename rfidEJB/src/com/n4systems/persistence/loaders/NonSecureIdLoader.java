package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class NonSecureIdLoader<T extends BaseEntity> extends Loader<T> {

	private final Class<T> clazz;
	private Long id;
	
	public NonSecureIdLoader(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	protected T load(EntityManager em) {
		QueryBuilder<T> builder = new QueryBuilder<T>(clazz, new OpenSecurityFilter());
		builder.addSimpleWhere("id", id);
		
		return builder.getSingleResult(em);
	}

	public NonSecureIdLoader<T> setId(Long id) {
		this.id = id;
		return this;
	}
}
