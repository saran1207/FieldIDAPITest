package com.n4systems.model.procedure;

import com.n4systems.model.Asset;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "procedures")
public class Procedure extends ArchivableEntityWithTenant implements NetworkEntity<Procedure> {

    @ManyToOne
    @JoinColumn(name = "type_id")
    @Deprecated // Do we need this??
    private ProcedureDefinition type;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(name="workflow_state", nullable=false)
    private ProcedureWorkflowState workflowState;

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

    @OneToMany
    @IndexColumn(name="orderIdx")
    @JoinTable(name="procedures_lock_results", joinColumns = @JoinColumn(name = "procedure_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_result_id"))
    private List<IsolationPointResult> lockResults;

    @OneToMany
    @IndexColumn(name="orderIdx")
    @JoinTable(name="procedures_unlock_results", joinColumns = @JoinColumn(name = "procedure_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_result_id"))
    private List<IsolationPointResult> unlockResults;

    public List<IsolationPointResult> getLockResults() {
        return lockResults;
    }

    public void setLockResults(List<IsolationPointResult> lockResults) {
        this.lockResults = lockResults;
    }

    @Deprecated
    public ProcedureDefinition getType() {
        return type;
    }

    @Deprecated
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

    public ProcedureWorkflowState getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(ProcedureWorkflowState workflowState) {
        this.workflowState = workflowState;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public List<IsolationPointResult> getUnlockResults() {
        return unlockResults;
    }

    public void setUnlockResults(List<IsolationPointResult> unlockResults) {
        this.unlockResults = unlockResults;
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
}
