package com.n4systems.model.procedure;

import com.n4systems.model.GpsLocation;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "procedures")
public class Procedure extends EntityWithTenant implements NetworkEntity<Procedure> {

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProcedureDefinition type;

    @Enumerated(EnumType.STRING)
    @Column(name="workflow_state", nullable=false)
    private WorkflowState workflowState;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "performedby_id")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_group_id")
    private UserGroup assignedGroup;

    @Column(name="completed_date")
    private Date completedDate;

    @Column(name="due_date")
    private Date dueDate;

    private GpsLocation gpsLocation;


    public ProcedureDefinition getType() {
        return type;
    }

    public void setType(ProcedureDefinition type) {
        this.type = type;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignedGroup = null;
        this.assignee = assignee;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(GpsLocation gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    @Override
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.LOCAL;
    }

    @Override
    public Procedure enhance(SecurityLevel level) {
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public UserGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(UserGroup assignedGroup) {
        this.assignee = null;
        this.assignedGroup = assignedGroup;
    }

    public WorkflowState getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(WorkflowState workflowState) {
        this.workflowState = workflowState;
    }
}
