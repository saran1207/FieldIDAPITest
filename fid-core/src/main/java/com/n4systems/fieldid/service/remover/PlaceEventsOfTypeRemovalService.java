package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class PlaceEventsOfTypeRemovalService extends FieldIdPersistenceService {

    @Transactional
    public EventArchiveSummary summary(PlaceEventType eventType) {
        EventArchiveSummary summary = new EventArchiveSummary();

        summary.setDeleteEvents(eventsToBeDeleted(eventType));
        summary.setDeleteSchedules(schedulesToBeDelete(eventType));

        return summary;
    }

    @Transactional
    public void remove(PlaceEventType eventType) {
        List<PlaceEvent> events = getEventsByEventType(eventType);
        for(PlaceEvent event: events) {
            event.retireEntity();
            persistenceService.update(event);
        }
    }

    private List<PlaceEvent> getEventsByEventType(EventType eventType) {
        QueryBuilder<PlaceEvent> queryBuilder = createTenantSecurityBuilder(PlaceEvent.class)
                .addSimpleWhere("type", eventType);
        return persistenceService.findAll(queryBuilder);
    }

    private Long schedulesToBeDelete(PlaceEventType eventType) {
        QueryBuilder<PlaceEvent> query = createTenantSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("type", eventType);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        query.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
        return persistenceService.count(query);
    }

    private Long eventsToBeDeleted(PlaceEventType eventType) {
        QueryBuilder<PlaceEvent> query = createTenantSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("type", eventType);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "workflowState", Arrays.asList(Event.WorkflowStateGrouping.NON_COMPLETE.getMembers())));
        return persistenceService.count(query);
    }

}
