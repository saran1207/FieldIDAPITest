package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.archivers.EventListArchiver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.TreeSet;

public class AllEventsOfTypeRemovalService extends FieldIdPersistenceService {

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
	public void remove(EventType eventType) {
		archiveEvents(eventType);
	}

	private void archiveEvents(EventType eventType) {
		List<Long> ids = getEventIds(eventType);

		archiveEvents(ids);
	}

	private void archiveEvents(List<Long> ids) {
		EventListArchiver archiver = new EventListArchiver(new TreeSet<Long>(ids));
		archiver.archive(entityManager);
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
		summary.setDeleteSchedules(scheduleListRemovalService.summary(null, eventType, Event.WorkflowStateGrouping.NON_COMPLETE).getSchedulesToRemove());

		return summary;
	}

	private Long eventToBeDeleted(EventType eventType) {
		String archiveQuery = "SELECT COUNT(id) FROM " + Event.class.getName() + " i WHERE i.type = :eventType AND i.state = :active AND i.workflowState = :workflowState";
		Query query = persistenceService.createQuery(archiveQuery);
		query.setParameter("eventType", eventType);
		query.setParameter("active", Archivable.EntityState.ACTIVE);
        query.setParameter("workflowState", WorkflowState.COMPLETED);

		return (Long)query.getSingleResult();
	}

}
