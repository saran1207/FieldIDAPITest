package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

public class AllEventsOfTypeRemovalService extends FieldIdPersistenceService {

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @Autowired
    private EventService eventService;

    @Transactional
	public void remove(EventType eventType) {
        List<ThingEvent> events = getEvents(eventType);
        for(Event event: events) {
            eventService.retireEvent((ThingEvent) event);
        }
	}

	private List<ThingEvent> getEvents(EventType eventType) {
		QueryBuilder<ThingEvent> queryBuilder = new QueryBuilder<ThingEvent>(ThingEvent.class, new TenantOnlySecurityFilter(getCurrentTenant()))
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
