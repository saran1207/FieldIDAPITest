package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.tools.FileDataContainer;

public class ProcedureAuditEventCreationService extends EventCreationService<ProcedureAuditEvent, ProcedureDefinition> {
    @Override
    protected ProcedureAuditEvent createEvent() {
        return new ProcedureAuditEvent();
    }

    @Override
    protected void setTargetFromScheduleBundle(ProcedureAuditEvent event, EventScheduleBundle<ProcedureDefinition> bundle) {
        event.setProcedureDefinition(bundle.getTarget());
    }

    @Override
    protected void preUpdateEvent(ProcedureAuditEvent event, FileDataContainer fileData) {
    }

    @Override
    protected void postUpdateEvent(ProcedureAuditEvent event, FileDataContainer fileData) {
    }
}
