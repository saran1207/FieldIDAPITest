package com.n4systems.model.location;

import javax.persistence.EntityManager;

import com.n4systems.persistence.savers.Saver;

public class PredefinedLocationSaver extends Saver<PredefinedLocation> {

	@Override
	protected void save(EntityManager em, PredefinedLocation entity) {
		super.save(em, entity);
		// See the PredefinedLocation entity for why this is here
		entity.rebuildSearchIds(em);
	}

}
