package com.n4systems.export;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;

public class EventsForAssetLoader {
	private final EntityManager em;
	
	public EventsForAssetLoader(EntityManager em) {
		this.em = em;
	}
	
	public List<Event> loadEvents(Asset asset) {
		String jpql = String.format("FROM %s e WHERE e.asset.id = :assetId ORDER BY e.date", Event.class.getName());
		TypedQuery<Event> query = em.createQuery(jpql, Event.class);
		query.setParameter("assetId", asset.getId());

		List<Event> events = query.getResultList();
		return events;
	}
}
