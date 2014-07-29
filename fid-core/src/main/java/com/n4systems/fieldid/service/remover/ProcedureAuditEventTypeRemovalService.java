package com.n4systems.fieldid.service.remover;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class ProcedureAuditEventTypeRemovalService extends FieldIdPersistenceService {

    public EventArchiveSummary summary(ProcedureAuditEventType eventType) {
        EventArchiveSummary summary = new EventArchiveSummary();

        summary.setDeleteEvents(eventsToBeDeleted(eventType));
        summary.setDeleteSchedules(schedulesToBeDelete(eventType));

        return summary;
    }

    @Transactional
    public void remove(ProcedureAuditEventType eventType) {
        List<Event> events = getEventsByEventType(eventType);
        for(Event event: events) {
            event.retireEntity();
            persistenceService.update(event);
        }
    }

    @Transactional
    public void removeRecurrence(ProcedureAuditEventType eventType) {
        List<RecurringLotoEvent> events = persistenceService.findAll(getRecurringLotoEventQuery(eventType));
        for( RecurringLotoEvent event: events) {
            event.retireEntity();
            persistenceService.update(event);
        }
    }

    private List<Event> getEventsByEventType(EventType eventType) {
        QueryBuilder<Event> queryBuilder = createTenantSecurityBuilder(Event.class)
                .addSimpleWhere("type", eventType);
        return persistenceService.findAll(queryBuilder);
    }

    public Long countRecurringEvents(ProcedureAuditEventType eventType) {
        QueryBuilder<RecurringLotoEvent> query = getRecurringLotoEventQuery(eventType);
        return persistenceService.count(query);
    }

    private QueryBuilder<RecurringLotoEvent> getRecurringLotoEventQuery(ProcedureAuditEventType eventType) {
        QueryBuilder<RecurringLotoEvent> query = createTenantSecurityBuilder(RecurringLotoEvent.class);
        query.addSimpleWhere("type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        query.addSimpleWhere("auditEventType", eventType);
        return query;
    }


    private Long schedulesToBeDelete(ProcedureAuditEventType eventType) {
        QueryBuilder<ProcedureAuditEvent> query = createTenantSecurityBuilder(ProcedureAuditEvent.class);
        query.addSimpleWhere("type", eventType);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        query.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
        return persistenceService.count(query);
    }

    private Long eventsToBeDeleted(ProcedureAuditEventType eventType) {
        QueryBuilder<ProcedureAuditEvent> query = createTenantSecurityBuilder(ProcedureAuditEvent.class);
        query.addSimpleWhere("type", eventType);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "workflowState", Arrays.asList(Event.WorkflowStateGrouping.NON_COMPLETE.getMembers())));
        return persistenceService.count(query);
    }
}
