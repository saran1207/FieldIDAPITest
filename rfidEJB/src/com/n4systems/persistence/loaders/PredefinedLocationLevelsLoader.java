package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class PredefinedLocationLevelsLoader extends SecurityFilteredLoader<PredefinedLocationLevels> {

	public PredefinedLocationLevelsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected PredefinedLocationLevels load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<PredefinedLocationLevels> queryBuilder = new QueryBuilder<PredefinedLocationLevels>(PredefinedLocationLevels.class, filter);
		PredefinedLocationLevels levels = queryBuilder.getSingleResult(em);
		
		return levels != null ? levels : new PredefinedLocationLevels();
	}

}
