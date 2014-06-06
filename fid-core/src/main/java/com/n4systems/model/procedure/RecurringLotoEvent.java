package com.n4systems.model.procedure;

import com.n4systems.model.Recurrence;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;

import javax.persistence.*;

@Entity
@Table(name = "recurring_loto_events")
@PrimaryKeyJoinColumn(name="id")
public class RecurringLotoEvent  extends ArchivableEntityWithTenant implements Saveable, SecurityEnhanced<RecurringLotoEvent>, Cloneable{

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
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


    public RecurringLotoEvent(ProcedureDefinition procedureDefinition, Assignable assignee, Recurrence recurrence) {
        this.procedureDefinition = procedureDefinition;
        setAssignedUserOrGroup(assignee);
        this.recurrence = recurrence;
    }

    public RecurringLotoEvent() {
        this(null, null, new Recurrence());
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

    @Override
    public RecurringLotoEvent enhance(SecurityLevel level) {
        return this;
    }
}
