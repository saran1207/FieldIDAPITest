package com.n4systems.model;

import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity
@Table(name="procedure_audit_events")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcedureAuditEvent extends Event<ProcedureAuditEventType, ProcedureAuditEvent, ProcedureDefinition> implements NetworkEntity<ProcedureAuditEvent>{

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "procedureDefinition.asset.owner", null, "state", false);
    }

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

    //We are forced to implement this due to the HasOwner interface on the event creation service and related classes
    @Override
    public BaseOrg getOwner() {
        return procedureDefinition.getOwner();
    }

    @Override
    public void setOwner(BaseOrg owner) {
        //should not have an owner
    }

    public ProcedureDefinition getProcedureDefinition() {
        return procedureDefinition;
    }

    public void  setProcedureDefinition(ProcedureDefinition procedureDefinition) {
        this.procedureDefinition = procedureDefinition;
    }

    public RecurringLotoEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringLotoEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }

    @Override
    @AllowSafetyNetworkAccess
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.calculateSecurityLevel(fromOrg, getProcedureDefinition().getAsset().getOwner());
    }

    @Override
    public ProcedureAuditEvent enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhance(this, level);
    }
}
