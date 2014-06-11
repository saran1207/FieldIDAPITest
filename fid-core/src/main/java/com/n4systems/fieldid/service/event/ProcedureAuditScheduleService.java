package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ProcedureAuditEvent;
import org.springframework.transaction.annotation.Transactional;

public class ProcedureAuditScheduleService extends FieldIdPersistenceService {

    @Transactional
    public Long createSchedule(ProcedureAuditEvent schedule) {
        Long id = persistenceService.save(schedule);
        //Update procedure def to notify mobile of change
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());
        return id;
    }

    @Transactional
    public ProcedureAuditEvent updateSchedule(ProcedureAuditEvent schedule) {
        ProcedureAuditEvent updatedSchedule = persistenceService.update(schedule);
        //Update procedure def to notify mobile of change
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());
        return updatedSchedule;
    }

    @Transactional
    public ProcedureAuditEvent retireSchedule(ProcedureAuditEvent schedule) {
        schedule.retireEntity();
        schedule = persistenceService.update(schedule);
        schedule.getProcedureDefinition().touch();
        persistenceService.update(schedule.getProcedureDefinition());
        return schedule;
    }

}
