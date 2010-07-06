package com.n4systems.model.location;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class PredefinedLocationListLoader extends ListLoader<PredefinedLocation> {

	private boolean parentFirstOrdering;

	public PredefinedLocationListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<PredefinedLocation> builder = createQueryBuilder(filter);
		if (parentFirstOrdering) {
			builder.addOrder("id");
		}
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<PredefinedLocation> createQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
	}

	public PredefinedLocationListLoader withParentFirstOrder() {
		parentFirstOrdering = true;
		return this;
	}

}
