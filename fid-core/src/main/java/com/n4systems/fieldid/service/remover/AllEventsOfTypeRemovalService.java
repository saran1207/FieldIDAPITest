package com.n4systems.fieldid.service.remover;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.archivers.EventListArchiver;
import com.n4systems.persistence.utils.LargeInClauseSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class AllEventsOfTypeRemovalService extends FieldIdPersistenceService {

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
	public void remove(EventType eventType) {
		archiveEvents(eventType);
        scheduleListRemovalService.remove(null, eventType, EventSchedule.ScheduleStatusGrouping.COMPLETE);
	}

	private void archiveEvents(EventType eventType) {
		List<Long> ids = getEventIds(eventType);

		archiveEvents(ids);
		updateAssetsLastEventDate(ids);
	}

	private void archiveEvents(List<Long> ids) {
		EventListArchiver archiver = new EventListArchiver(new TreeSet<Long>(ids));
		archiver.archive(entityManager);
	}

	private void updateAssetsLastEventDate(List<Long> ids) {
		List<Long> assetsToUpdateEventDate = new LargeInClauseSelect<Long>( new QueryBuilder<Long>(Event.class, new OpenSecurityFilter())
				.setSimpleSelect("asset.id", true)
				.addSimpleWhere("asset.state", Archivable.EntityState.ACTIVE),
		  ids,
		  entityManager).getResultList();

		for (Long assetId : new HashSet<Long>(assetsToUpdateEventDate)) {
			Asset asset = entityManager.find(Asset.class, assetId);


			QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");
			qBuilder.setMaxSelect("schedule.completedDate");
			qBuilder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
			qBuilder.addSimpleWhere("asset", asset);


			Date lastEventDate = null;
			try {
				lastEventDate = qBuilder.getSingleResult(entityManager);
			} catch (Exception e) {
				throw new ProcessFailureException("could not archive the events", e);
			}

			asset.setLastEventDate(lastEventDate);

			entityManager.merge(asset);
		}
	}

	private List<Long> getEventIds(EventType eventType) {
		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(Event.class, new TenantOnlySecurityFilter(getCurrentTenant()))
				.setSelectArgument(new SimpleSelect("id"))
				.addSimpleWhere("type", eventType);
        return persistenceService.findAll(queryBuilder);
	}

    @Transactional
	public EventArchiveSummary summary(EventType eventType) {
		EventArchiveSummary summary = new EventArchiveSummary();

		summary.setDeleteEvents(eventToBeDeleted(eventType));
		summary.setDeleteSchedules(scheduleListRemovalService.summary(null, eventType, EventSchedule.ScheduleStatusGrouping.COMPLETE).getSchedulesToRemove());

		return summary;
	}

	private Long eventToBeDeleted(EventType eventType) {
		String archiveQuery = "SELECT COUNT(id) FROM " + Event.class.getName() + " i WHERE i.type = :eventType AND i.state = :active";
		Query query = persistenceService.createQuery(archiveQuery);
		query.setParameter("eventType", eventType);
		query.setParameter("active", Archivable.EntityState.ACTIVE);

		return (Long)query.getSingleResult();
	}

}
