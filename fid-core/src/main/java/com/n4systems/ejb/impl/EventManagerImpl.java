package com.n4systems.ejb.impl;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.WSSearchCritiera;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventManagerImpl implements EventManager {
	static Logger logger = Logger.getLogger(EventManagerImpl.class);

	private EntityManager em;

	private final PersistenceManager persistenceManager;
	private final ManagerBackedEventSaver eventSaver;

	private final EntityManagerLastEventDateFinder lastEventFinder;
	private final EventScheduleManager eventScheduleManager;

	public EventManagerImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.lastEventFinder = new EntityManagerLastEventDateFinder(persistenceManager, em);
		this.eventScheduleManager = new EventScheduleManagerImpl(em);
		this.eventSaver = new ManagerBackedEventSaver(new LegacyAssetManager(em),
				persistenceManager, em, lastEventFinder);
	}

	
	public ThingEvent findEventThroughSubEvent(Long subEventId, SecurityFilter filter) {
		String str = "SELECT e FROM "+ThingEvent.class.getName()+" e JOIN FETCH e.asset, IN( e.subEvents ) s WHERE s.id = :subEventId AND ";
		str += filter.produceWhereClause(ThingEvent.class, "e");
		Query query = em.createQuery(str);
		query.setParameter("subEventId", subEventId);
		filter.applyParameters(query, ThingEvent.class);
		try {
            ThingEvent event = (ThingEvent) query.getSingleResult();
			return event;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub event attached", e);
			return null;
		}
	}

	public SubEvent findSubEvent(Long subEventId, SecurityFilter filter) {
        ThingEvent event = findEventThroughSubEvent(subEventId, filter);

		if (event == null) {
			return null;
		}

		try {
            SubEvent subEvent = persistenceManager.find(SubEvent.class, subEventId, "attachments");
            subEvent.setAsset(event.getAsset());
            return subEvent;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub event attached ", e);
			return null;
		}
	}

    public PlaceEvent findPlaceEvent(Long id, SecurityFilter filter) {
        PlaceEvent event = null;

        QueryBuilder<PlaceEvent> queryBuilder = new QueryBuilder<PlaceEvent>(PlaceEvent.class, filter);
        queryBuilder.setSimpleSelect().addSimpleWhere("id", id).addSimpleWhere("state", EntityState.ACTIVE);
        queryBuilder.addOrder("created");
        queryBuilder.addPostFetchPaths(Event.PLACE_FIELD_PATHS);

        try {
            event = persistenceManager.find(queryBuilder);
        } catch (InvalidQueryException iqe) {
            logger.error("bad query while loading place event", iqe);
        }

        return event;
    }

	public ThingEvent findAllFields(Long id, SecurityFilter filter) {
        ThingEvent event = null;

		QueryBuilder<ThingEvent> queryBuilder = new QueryBuilder<ThingEvent>(ThingEvent.class, filter);
		queryBuilder.setSimpleSelect().addSimpleWhere("id", id).addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addOrder("created");
		queryBuilder.addPostFetchPaths(Event.ALL_FIELD_PATHS_WITH_SUB_EVENTS);

		try {
			event = persistenceManager.find(queryBuilder);
            event.getAsset();
            if (event != null) {
				// now we need to postfetch the rec/def lists on the results. We
				// can't do this above since the results themselvs are a list.
				persistenceManager.postFetchFields(event.getResults(), "recommendations", "deficiencies");
                if (event.getType().isThingEventType()) {
                    persistenceManager.postFetchFields(event, Event.THING_TYPE_PATHS);
                }

			}

		} catch (InvalidQueryException iqe) {
			logger.error("bad query while loading event", iqe);
		}

		return event;
	}

	public List<ThingEvent> findEventsByDateAndAsset(Date datePerformedRangeStart, Date datePerformedRangeEnd, Asset asset, SecurityFilter filter) {
		

		QueryBuilder<ThingEvent> queryBuilder = new QueryBuilder<ThingEvent>(ThingEvent.class, filter);
		queryBuilder.setSimpleSelect();
		queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addWhere(Comparator.GE, "beginingDate", "completedDate", datePerformedRangeStart).addWhere(Comparator.LE, "endingDate", "completedDate", datePerformedRangeEnd);
		queryBuilder.addSimpleWhere("asset", asset);

		List<ThingEvent> events;
		try {
			events = persistenceManager.findAll(queryBuilder);
		} catch (InvalidQueryException e) {
			events = new ArrayList<ThingEvent>();
			logger.error("Unable to load Events by Date and Asset", e);
		}

		return events;
	}


	

	
	public ThingEvent updateEvent(ThingEvent event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		return eventSaver.updateEvent(event, scheduleId, userId, fileData, uploadedFiles);
	}



	public Event retireEvent(Event event, Long userId) {
		event.retireEntity();
		event = persistenceManager.update(event, userId);
        if (event instanceof ThingEvent) {
            ((ThingEvent)event).setAsset(persistenceManager.update(((ThingEvent)event).getAsset()));
        }
        persistenceManager.update(event);
		return event;
	}

	/**
	 * This must be called AFTER the event and subevent have been persisted
	 */
	public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return eventSaver.attachFilesToSubEvent(event, subEvent, uploadedFiles);
	}

	public Pager<ThingEvent> findNewestEvents(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {

		List<Long> customerIds = searchCriteria.getCustomerIds();
		List<Long> divisionIds = searchCriteria.getDivisionIds();

		boolean setCustomerInfo = (customerIds != null && customerIds.size() > 0);
		boolean setDivisionInfo = (divisionIds != null && divisionIds.size() > 0);

		String selectStatement = " from "+Event.class.getName()+" i ";

		String whereClause = "where ( ";
		if (setCustomerInfo) {
			whereClause += "i.asset.owner.customerOrg.id in (:customerIds) ";

			if (setDivisionInfo) {
				whereClause += "or ";
			}
		}

		if (setDivisionInfo) {
			whereClause += "i.asset.owner.divisionOrg.id in (:divisionIds)";
		}

		whereClause += ") AND i.asset.lastEventDate = i.date and " + securityFilter.produceWhereClause(Event.class, "i") + ")";

		Query query = em.createQuery("select i " + selectStatement + whereClause + " ORDER BY i.id");
		if (setCustomerInfo)
			query.setParameter("customerIds", customerIds);
		if (setDivisionInfo)
			query.setParameter("divisionIds", divisionIds);
		securityFilter.applyParameters(query, Event.class);

		Query countQuery = em.createQuery("select count( i.id ) " + selectStatement + whereClause);
		if (setCustomerInfo)
			countQuery.setParameter("customerIds", customerIds);
		if (setDivisionInfo)
			countQuery.setParameter("divisionIds", divisionIds);
		securityFilter.applyParameters(countQuery, Event.class);

		return new Page<ThingEvent>(query, countQuery, page, pageSize);
	}

//	public Pager<Event> findNewestEvents(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {
//		QueryBuilder<Event> qb = new QueryBuilder<Event>(Event.class, securityFilter, "e");
//		qb.addWhere(new PassthruWhereClause("latestEventClause", "e.asset.lastEventDate = e.date"));
//
//		PassthruWhereClause jobClause = new PassthruWhereClause("jobClause", "e.asset.id IN (SELECT s.asset.id FROM Project p, IN (p.schedules) s WHERE p.id IN (:jobIds))");
//		jobClause.getParams().put("jobIds", searchCriteria.getJobIds());
//		qb.addWhere(jobClause);
//
//		Pager<Event> eventPage = qb.getPaginatedResults(em, page, pageSize);
//		return eventPage;
//	}

	public boolean isMasterEvent(Long id) {
		Event event = em.find(Event.class, id);

		return (event != null) ? (!event.getSubEvents().isEmpty()) : false;
	}

    @CopiedToService(LastEventDateService.class)
	public Date findLastEventDate(Long eventId) {
		return lastEventFinder.findLastEventDate(eventId);
	}

}
