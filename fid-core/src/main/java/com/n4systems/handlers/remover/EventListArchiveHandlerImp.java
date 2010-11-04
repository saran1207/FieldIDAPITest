package com.n4systems.handlers.remover;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.archivers.EventListArchiver;
import com.n4systems.persistence.utils.LargeInClauseSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;


public class EventListArchiveHandlerImp implements EventTypeListArchiveHandler {

	private final ScheduleListDeleteHandler scheduleListDeleter;
	private EventType eventType;

	public EventListArchiveHandlerImp(ScheduleListDeleteHandler scheduleListDeleter) {
		this.scheduleListDeleter = scheduleListDeleter;
	}
	
	public void remove(Transaction transaction) {
		archiveEvents(transaction.getEntityManager());
		scheduleListDeleter.targetCompleted().setEventType(eventType).remove(transaction);
	}

	private void archiveEvents(EntityManager em) {
		List<Long> ids = getEventIds(em);
		
		archiveEvents(em, ids);
		updateAssetsLastEventDate(em, ids);
	}

	private void archiveEvents(EntityManager em, List<Long> ids) {
		EventListArchiver archiver = new EventListArchiver(new TreeSet<Long>(ids));
		archiver.archive(em);
	}

	private void updateAssetsLastEventDate(EntityManager em, List<Long> ids) {
		
		List<Long> assetsToUpdateEventDate = new LargeInClauseSelect<Long>( new QueryBuilder<Long>(Event.class, new OpenSecurityFilter())
				.setSimpleSelect("asset.id", true)
				.addSimpleWhere("asset.state", EntityState.ACTIVE),
		  ids,
		  em).getResultList();
		
		for (Long assetId : new HashSet<Long>(assetsToUpdateEventDate)) {
			Asset asset = em.find(Asset.class, assetId);
			
			
			QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");
			qBuilder.setMaxSelect("date");
			qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			qBuilder.addSimpleWhere("asset", asset);


			Date lastEventDate = null;
			try {
				lastEventDate = qBuilder.getSingleResult(em);
			} catch (Exception e) {
				throw new ProcessFailureException("could not archive the events", e);
			}

			asset.setLastEventDate(lastEventDate);
			
			em.merge(asset);
		}
	}

	private List<Long> getEventIds(EntityManager em) {
		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter())
				.setSelectArgument(new SimpleSelect("id"))
				.addSimpleWhere("type", eventType);
		return queryBuilder.getResultList(em);
	}
	
	public EventArchiveSummary summary(Transaction transaction) {
		EventArchiveSummary summary = new EventArchiveSummary();
		
		summary.setDeleteEvents(eventToBeDeleted(transaction));
		summary.setDeleteSchedules(scheduleListDeleter.targetCompleted().setEventType(eventType).summary(transaction).getSchedulesToRemove());
		
		return summary;
	}

	private Long eventToBeDeleted(Transaction transaction) {
		String archiveQuery = "SELECT COUNT(id) FROM " + Event.class.getName() + " i WHERE i.type = :eventType AND i.state = :active";
		Query query = transaction.getEntityManager().createQuery(archiveQuery);
		query.setParameter("eventType", eventType);
		query.setParameter("active", EntityState.ACTIVE);
		
		return (Long)query.getSingleResult();
	}
	
	public EventListArchiveHandlerImp setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
	
}
