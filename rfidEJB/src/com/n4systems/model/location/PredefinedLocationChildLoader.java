package com.n4systems.model.location;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class PredefinedLocationChildLoader extends ListLoader<PredefinedLocation> {

	private Long parentId;
	
	public PredefinedLocationChildLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<PredefinedLocation> builder = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
		builder.addWhere(WhereClauseFactory.create("parent.id", parentId));
		
		List<PredefinedLocation> locations = builder.getResultList(em);
		return locations;
	}

	public PredefinedLocationChildLoader setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}
}
