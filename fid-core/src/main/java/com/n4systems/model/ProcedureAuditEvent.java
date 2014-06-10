package com.n4systems.model;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.RecurringLotoEvent;
import org.hibernate.annotations.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="procedure_audit_events")
@PrimaryKeyJoinColumn(name="id")
public class ProcedureAuditEvent extends Event<ProcedureAuditEventType, ProcedureAuditEvent, ProcedureDefinition> {

    @ManyToOne
    @JoinColumn(name="procedure_definition_id")
    private ProcedureDefinition procedureDefinition;

    @ManyToOne
    @JoinColumn(name="recurring_event_id")
    private RecurringLotoEvent recurringEvent;

    @Override
    protected void copyDataIntoResultingAction(AbstractEvent<?, ?> event) {
        ProcedureAuditEvent action = (ProcedureAuditEvent) event;
        action.setProcedureDefinition(getProcedureDefinition());
    }

    @Override
    public ProcedureDefinition getTarget() {
        return procedureDefinition;
    }

    @Override
    public void setTarget(ProcedureDefinition target) {
        setProcedureDefinition(target);
    }

    @Override
    public BaseOrg getOwner() {
        return null;
    }

    @Override
    public void setOwner(BaseOrg owner) {

    }

    public ProcedureDefinition getProcedureDefinition() {
        return procedureDefinition;
    }

    public void setProcedureDefinition(ProcedureDefinition procedureDefinition) {
        this.procedureDefinition = procedureDefinition;
    }

    public RecurringLotoEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringLotoEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }
}
