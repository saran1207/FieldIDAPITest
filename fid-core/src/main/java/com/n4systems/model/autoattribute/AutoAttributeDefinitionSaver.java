package com.n4systems.model.autoattribute;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class AutoAttributeDefinitionSaver extends Saver<AutoAttributeDefinition> {

	@Override
	public void remove(EntityManager em, AutoAttributeDefinition entity) {
		super.remove(em, entity);
		updateCriteria(em, entity);
	}

	@Override
	public void save(EntityManager em, AutoAttributeDefinition entity) {
		update(em, entity);
	}

	@Override
	public AutoAttributeDefinition update(EntityManager em, AutoAttributeDefinition entity) {
		/*
		 * This save process comes directly from the old AutoAttributeManagerImpl.  Not sure if it's still required.
		 * 1. Definitions are never persisted, only merged.  Notice, save just calls this.
		 * 2. After the definition is saved, its criteria is loaded, touched and merged.
		 */
		AutoAttributeDefinition mergedEntity = super.update(em, entity);
		updateCriteria(em, mergedEntity);
		return mergedEntity;
	}

	private void updateCriteria(EntityManager em, AutoAttributeDefinition entity) {
		AutoAttributeCriteria criteria = em.find(AutoAttributeCriteria.class, entity.getCriteria().getId());
		criteria.touch();
		em.merge(criteria);
	}
	
}
