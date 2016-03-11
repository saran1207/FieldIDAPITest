package com.n4systems.persistence.loaders;

import com.n4systems.model.security.SecurityFilter;

import javax.persistence.EntityManager;
import java.util.List;

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
