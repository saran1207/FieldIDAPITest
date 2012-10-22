package com.n4systems.ejb.impl;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.Date;

@Deprecated
@CopiedToService(LastEventDateService.class)
public class EntityManagerLastEventDateFinder implements LastEventDateFinder {
	private static Logger logger = Logger.getLogger(EntityManagerLastEventDateFinder.class);
	
	
	private final PersistenceManager persistenceManager;
	private final EntityManager em;

	public EntityManagerLastEventDateFinder(PersistenceManager persistenceManager, EntityManager em) {
		this.persistenceManager = persistenceManager;
		this.em = em;
	}

    @CopiedToService(LastEventDateService.class)
	public Date findLastEventDate(Asset asset) {
		return findLastEventDate(asset, null);
	}

    @Override
    public Date findLastEventDate(Long eventId) {
        Event event = persistenceManager.find(Event.class, eventId);
        return findLastEventDate(event.getAsset(),event.getType());
    }

    @CopiedToService(LastEventDateService.class)
	public Date findLastEventDate(Asset asset, EventType eventType) {

		QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");

		qBuilder.setMaxSelect("completedDate");
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
