package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;


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

	/**
	 * finds all the groups that you can view with the defined security filter.
	 */
	@SuppressWarnings("unchecked")
	private List<EventGroup> findAllEventGroups(SecurityFilter userFilter, Long assetId) {
		ManualSecurityFilter filter = new ManualSecurityFilter(userFilter);
		filter.setTargets("eg.tenant.id", "event.owner", null, null);

		String queryString = "Select DISTINCT eg FROM "+ EventGroup.class.getName()+" as eg INNER JOIN eg.events as event LEFT JOIN event.asset as asset"
				+ " WHERE asset.id = :id AND event.state = :activeState  AND " + filter.produceWhereClause() + " ORDER BY eg.created ";

		Query query = em.createQuery(queryString);

		filter.applyParameters(query);
		query.setParameter("id", assetId);
		query.setParameter("activeState", EntityState.ACTIVE);

		return query.getResultList();
	}

	/**
	 * finds all the groups that you can view with the defined security filter.
	 */
	public List<EventGroup> findAllEventGroups(SecurityFilter filter, Long assetId, String... postFetchFields) {
		return (List<EventGroup>) persistenceManager.postFetchFields(findAllEventGroups(filter, assetId), postFetchFields);
	}

	
	public Event findEventThroughSubEvent(Long subEventId, SecurityFilter filter) {
		String str = "select e FROM "+Event.class.getName()+" e, IN( e.subEvents ) s WHERE s.id = :subEventId AND ";
		str += filter.produceWhereClause(Event.class, "e");
		Query query = em.createQuery(str);
		query.setParameter("subEventId", subEventId);
		filter.applyParameters(query, Event.class);
		try {
			return (Event) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub event attached", e);
			return null;
		}
	}

	public SubEvent findSubEvent(Long subEventId, SecurityFilter filter) {
		Event event = findEventThroughSubEvent(subEventId, filter);

		if (event == null) {
			return null;
		}

		try {
			return persistenceManager.find(SubEvent.class, subEventId, "attachments");
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub event attached ", e);
			return null;
		}
	}

	public Event findAllFields(Long id, SecurityFilter filter) {
		Event event = null;

		QueryBuilder<Event> queryBuilder = new QueryBuilder<Event>(Event.class, filter);
		queryBuilder.setSimpleSelect().addSimpleWhere("id", id).addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addOrder("created");
		queryBuilder.addPostFetchPaths(Event.ALL_FIELD_PATHS_WITH_SUB_EVENTS);

		try {
			event = persistenceManager.find(queryBuilder);
			if (event != null) {
				// now we need to postfetch the rec/def lists on the results. We
				// can't do this above since the results themselvs are a list.
				persistenceManager.postFetchFields(event.getResults(), "recommendations", "deficiencies");

			}

		} catch (InvalidQueryException iqe) {
			logger.error("bad query while loading event", iqe);
		}

		return event;
	}

	public List<Event> findEventsByDateAndAsset(Date datePerformedRangeStart, Date datePerformedRangeEnd, Asset asset, SecurityFilter filter) {
		

		QueryBuilder<Event> queryBuilder = new QueryBuilder<Event>(Event.class, filter);
		queryBuilder.setSimpleSelect();
		queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addWhere(Comparator.GE, "beginingDate", "date", datePerformedRangeStart).addWhere(Comparator.LE, "endingDate", "date", datePerformedRangeEnd); 
		queryBuilder.addSimpleWhere("asset", asset);

		List<Event> events;
		try {
			events = persistenceManager.findAll(queryBuilder);
		} catch (InvalidQueryException e) {
			events = new ArrayList<Event>();
			logger.error("Unable to load Events by Date and Asset", e);
		}

		return events;
	}


	

	
	public Event updateEvent(Event event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		return eventSaver.updateEvent(event, scheduleId, userId, fileData, uploadedFiles);
	}



	public Event retireEvent(Event event, Long userId) {
		event.retireEntity();
		event = persistenceManager.update(event, userId);
		eventSaver.updateAssetLastEventDate(event.getAsset());
		event.setAsset(persistenceManager.update(event.getAsset()));
		eventScheduleManager.restoreScheduleForEvent(event);
        persistenceManager.update(event);
		return event;
	}

	/**
	 * This must be called AFTER the event and subevent have been persisted
	 */
	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return eventSaver.attachFilesToSubEvent(event, subEvent, uploadedFiles);
	}

	public Pager<Event> findNewestEvents(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {

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

		return new Page<Event>(query, countQuery, page, pageSize);
	}

	public Pager<Event> findNewestEvents(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {

		List<Long> jobIds = searchCriteria.getJobIds();

		String selectStatement = " from "+Event.class.getName()+" i ";

		String whereClause = "where ( ";
		
		whereClause += "i.asset.id in (select sch.asset.id from Project p, IN (p.schedules) sch where p.id in (:jobIds))";

		whereClause += ") AND i.asset.lastEventDate = i.date and " + securityFilter.produceWhereClause(EventGroup.class, "i") + ")";

		Query query = em.createQuery("select i " + selectStatement + whereClause + " ORDER BY i.id");
		query.setParameter("jobIds", jobIds);
		securityFilter.applyParameters(query, EventGroup.class);

		Query countQuery = em.createQuery("select count( i.id ) " + selectStatement + whereClause);
		countQuery.setParameter("jobIds", jobIds);
		securityFilter.applyParameters(countQuery, EventGroup.class);

		return new Page<Event>(query, countQuery, page, pageSize);
	}
	
	public boolean isMasterEvent(Long id) {
		Event event = em.find(Event.class, id);

		return (event != null) ? (!event.getSubEvents().isEmpty()) : false;
	}

	public Date findLastEventDate(EventSchedule schedule) {
		return lastEventFinder.findLastEventDate(schedule);
	}

	public Date findLastEventDate(Long scheduleId) {
		return lastEventFinder.findLastEventDate(scheduleId);
	}

}
