package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.tools.FileDataContainer;

import java.util.List;

public class ProcedureAuditEventCreationService extends EventCreationService<ProcedureAuditEvent, ProcedureDefinition> {
    @Override
    protected ProcedureAuditEvent createEvent() {
        return new ProcedureAuditEvent();
    }

    @Override
    protected void setTargetFromScheduleBundle(ProcedureAuditEvent event, EventScheduleBundle<ProcedureDefinition> bundle) {
        event.setProcedureDefinition(bundle.getTarget());
    }

    /**
     * This Override has to be made to step around problems with Java 8u20, which has problems with class resolution and
     * experiences an AssertionError when trying to determine what T is a SubClass of.  While this problem is not
     * present in later versions of Java 8, we are using Java 8u20 on the production server... I don't want to risk
     * making things explode.
     *
     * @param event - A ProcedureAuditEvent that you want to update.
     * @param fileData - A FileDataContainer containing file data.
     * @param attachments - A List of FileAttachment objects to
     * @return A set of training wheels that we had to introduce to handhold Java through this fault.
     */
    @Override
    public ProcedureAuditEvent updateEvent(ProcedureAuditEvent event, FileDataContainer fileData, List<FileAttachment> attachments) {
        ProcedureAuditEvent trainingWheels = super.updateEvent(event, fileData, attachments);

        ruleService.clearEscalationRulesForEvent(trainingWheels.getId());
        if(trainingWheels.getWorkflowState().equals(WorkflowState.OPEN)) {
            ruleService.createApplicableQueueItems(trainingWheels);
        }

        return trainingWheels;
    }

    @Override
    protected void preUpdateEvent(ProcedureAuditEvent event, FileDataContainer fileData) {
    }

    @Override
    protected void postUpdateEvent(ProcedureAuditEvent event, FileDataContainer fileData) {
    }
}
