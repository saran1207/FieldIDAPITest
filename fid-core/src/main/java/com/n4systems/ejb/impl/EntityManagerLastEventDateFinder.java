package com.n4systems.ejb.impl;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.EventSchedule;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class EntityManagerLastEventDateFinder implements LastEventDateFinder {
	private static Logger logger = Logger.getLogger(EntityManagerLastEventDateFinder.class);
	
	
	private final PersistenceManager persistenceManager;
	private final EntityManager em;

	public EntityManagerLastEventDateFinder(PersistenceManager persistenceManager, EntityManager em) {
		super();
		this.persistenceManager = persistenceManager;
		this.em = em;
	}

	public Date findLastEventDate(Asset asset) {
		return findLastEventDate(asset, null);
	}

	public Date findLastEventDate(Long scheduleId) {
		return findLastEventDate(persistenceManager.find(EventSchedule.class, scheduleId));
	}

	public Date findLastEventDate(EventSchedule schedule) {
		return findLastEventDate(schedule.getAsset(), schedule.getEventType());
	}

	public Date findLastEventDate(Asset asset, EventType eventType) {

		QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");

		qBuilder.setMaxSelect("date");
		qBuilder.addSimpleWhere("asset.id", asset.getId());
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);

		if (eventType != null) {
			qBuilder.addSimpleWhere("type.id", eventType.getId());
		}

		Date lastEventDate = null;
		try {
			lastEventDate = qBuilder.getSingleResult(em);
		} catch (InvalidQueryException e) {
			logger.error("Unable to find last event date", e);
		} catch (Exception e) {
			logger.error("Unable to find last event date", e);
		}

		return lastEventDate;
	}

}
