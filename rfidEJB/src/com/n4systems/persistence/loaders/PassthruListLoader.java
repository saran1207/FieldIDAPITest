package com.n4systems.persistence.loaders;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;

public class PassthruListLoader<T> extends ListLoader<T> {
	private final List<T> entities;
	
	public PassthruListLoader(List<T> entities) {
		super(null);
		this.entities = entities;
	}

	@Override
	protected List<T> load(EntityManager em, SecurityFilter filter) {
		return entities;
	}

}
