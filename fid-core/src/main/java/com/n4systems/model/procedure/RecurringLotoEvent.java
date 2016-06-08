package com.n4systems.model.procedure;

import com.n4systems.model.ProcedureAuditEventType;
import com.n4systems.model.Recurrence;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "recurring_loto_events")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecurringLotoEvent  extends ArchivableEntityWithTenant implements Saveable, SecurityEnhanced<RecurringLotoEvent>, Cloneable{

    public enum RecurringLotoEventType implements DisplayEnum{
        LOTO("Lockout"), AUDIT("Procedure Audit");

        private String label;

        RecurringLotoEventType(String label) {
            this.label = label;
        }

        @Override
        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "procedure_definition_id", nullable = true)
    private ProcedureDefinition procedureDefinition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recurrence_id")
    private Recurrence recurrence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="assigned_group_id")
    private UserGroup assignedGroup;

    @Enumerated(EnumType.STRING)
    private RecurringLotoEventType type;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name="audit_event_id")
    private ProcedureAuditEventType auditEventType;


    public RecurringLotoEvent(ProcedureAuditEventType auditEventType, ProcedureDefinition procedureDefinition, Assignable assignee, Recurrence recurrence) {
        this.auditEventType = auditEventType;
        this.procedureDefinition = procedureDefinition;
        setAssignedUserOrGroup(assignee);
        this.recurrence = recurrence;
        setType(RecurringLotoEventType.AUDIT);
    }

    public RecurringLotoEvent(ProcedureDefinition procedureDefinition, Assignable assignee, Recurrence recurrence) {
        this.procedureDefinition = procedureDefinition;
        setAssignedUserOrGroup(assignee);
        this.recurrence = recurrence;
        setType(RecurringLotoEventType.LOTO);
    }

    public RecurringLotoEvent() {
        this(null, null, null, new Recurrence());
    }

    public ProcedureDefinition getProcedureDefinition() {
        return procedureDefinition;
    }

    public void setProcedureDefinition(ProcedureDefinition procedureDefinition) {
        this.procedureDefinition = procedureDefinition;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public UserGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(UserGroup assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    @Transient
    public Assignable getAssignedUserOrGroup() {
        return assignee != null ?  assignee : assignedGroup;
    }

    public void setAssignedUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setAssignee((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setAssignedGroup((UserGroup) assignee);
        } else if (assignee == null) {
            this.assignee = null;
            this.assignedGroup = null;
        }
    }
    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public RecurringLotoEventType getType() {
        return type;
    }

    public void setType(RecurringLotoEventType type) {
        this.type = type;
    }

    public ProcedureAuditEventType getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(ProcedureAuditEventType auditEventType) {
        this.auditEventType = auditEventType;
    }

    @Override
    public RecurringLotoEvent enhance(SecurityLevel level) {
        return this;
    }

    public boolean isRecurringLockout() {
        return type.equals(RecurringLotoEventType.LOTO);
    }

    public boolean isRecurringAudit() {
        return type.equals(RecurringLotoEventType.AUDIT);
    }

}
