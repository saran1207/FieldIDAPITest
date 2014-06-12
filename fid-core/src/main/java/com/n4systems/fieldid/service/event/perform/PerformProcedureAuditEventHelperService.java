package com.n4systems.fieldid.service.event.perform;

import com.n4systems.model.Event;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.ProcedureAuditEventType;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.persistence.utils.PostFetcher;

public class PerformProcedureAuditEventHelperService extends PerformEventHelperService<ProcedureAuditEvent, ProcedureAuditEventType>{

    public PerformProcedureAuditEventHelperService() {
        super(ProcedureAuditEvent.class, ProcedureAuditEventType.class);
    }

    @Override
    protected ProcedureAuditEvent newEvent() {
        return new ProcedureAuditEvent();
    }

    @Override
    protected ProcedureAuditEvent createNewEvent(ProcedureAuditEvent event, Long targetId, Long eventTypeId) {
        ProcedureAuditEvent newEvent = super.createNewEvent(event, targetId, eventTypeId);
        event.setProcedureDefinition(persistenceService.find(ProcedureDefinition.class, targetId));
        return newEvent;
    }

    @Override
    protected void postFetchAdditionalFields(ProcedureAuditEvent event) {
        PostFetcher.postFetchFields(event, Event.PROCEDURE_AUDIT_FIELD_PATHS);
    }
}
