package com.n4systems.model.orders;

import com.n4systems.model.LineItem;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class NonIntegrationLineItemSaver extends Saver<LineItem> {

	@Override
	public void save(EntityManager em, LineItem entity) {
		em.persist(entity.getOrder());
		super.save(em, entity);
	}

	@Override
	public LineItem update(EntityManager em, LineItem entity) {
		entity.setOrder(em.merge(entity.getOrder()));
		return super.update(em, entity);
	}

}
