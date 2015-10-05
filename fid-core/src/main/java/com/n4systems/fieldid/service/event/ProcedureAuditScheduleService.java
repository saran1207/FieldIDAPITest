package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.WorkflowState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ProcedureAuditScheduleService extends FieldIdPersistenceService {

    @Autowired private AssignmentEscalationRuleService ruleService;

    @Transactional
    public Long createSchedule(ProcedureAuditEvent schedule) {
        Long id = persistenceService.save(schedule);
        //Update procedure def to notify mobile of change
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());

        ruleService.createApplicableQueueItems(schedule);

        return id;
    }

    @Transactional
    public ProcedureAuditEvent updateSchedule(ProcedureAuditEvent schedule) {
        ProcedureAuditEvent updatedSchedule = persistenceService.update(schedule);
        //Update procedure def to notify mobile of change
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());

        ruleService.clearEscalationRulesForEvent(updatedSchedule.getId());
        if(updatedSchedule.getWorkflowState().equals(WorkflowState.OPEN)) {
            ruleService.createApplicableQueueItems(updatedSchedule);
        }

        return updatedSchedule;
    }

    @Transactional
    public ProcedureAuditEvent retireSchedule(ProcedureAuditEvent schedule) {
        schedule.retireEntity();
        schedule = persistenceService.update(schedule);
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());

        ruleService.clearEscalationRulesForEvent(schedule.getId());

        return schedule;
    }

}
