package com.n4systems.model.procedure;

import com.n4systems.model.Asset;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "procedures")
public class Procedure extends ArchivableEntityWithTenant implements NetworkEntity<Procedure>, HasGpsLocation {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", false);
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    // This is set when we perform the lock.
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
    @JoinColumn(name = "lockedby_id")
    private User lockedBy;

    @ManyToOne
    @JoinColumn(name = "unlockedby_id")
    private User unlockedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_group_id")
    private UserGroup assignedGroup;

    @Column(name="lock_date")
    private Date lockDate;

    @Column(name="unlock_date")
    private Date unlockDate;

    @Column(name="due_date")
    private Date dueDate;

    private GpsLocation gpsLocation;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name="orderIdx")
    @JoinTable(name="procedures_lock_results", joinColumns = @JoinColumn(name = "procedure_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_result_id"))
    private List<IsolationPointResult> lockResults;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name="orderIdx")
    @JoinTable(name="procedures_unlock_results", joinColumns = @JoinColumn(name = "procedure_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_result_id"))
    private List<IsolationPointResult> unlockResults;

    @Column(name="mobileguid")
    private String mobileGUID;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="recurring_event_id")
    private RecurringLotoEvent recurringEvent;

    @ManyToOne
    @JoinColumn(name = "lockout_reason_id")
    private LockoutReason lockoutReason;

    @OneToOne(mappedBy = "procedure", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private ProcedureNotification notification;

    @Column(name="send_email_on_update")
    private Boolean sendEmailOnUpdate = Boolean.TRUE;

    @Transient
    private Boolean assigneeOrDateChanged = Boolean.FALSE;

    public List<IsolationPointResult> getLockResults() {
        return lockResults;
    }

    public void setLockResults(List<IsolationPointResult> lockResults) {
        this.lockResults = lockResults;
    }

    public ProcedureDefinition getType() {
        return type;
    }

    public void setType(ProcedureDefinition type) {
        this.type = type;
    }

    public LockoutReason getLockoutReason() {
        return lockoutReason;
    }

    public void setLockoutReason(LockoutReason lockoutReason) {
        this.lockoutReason = lockoutReason;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignedGroup = null;
        this.assignee = assignee;
    }

    public User getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(User lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
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

    public String getMobileGUID() {
        return mobileGUID;
    }

    public void setMobileGUID(String mobileGUID) {
        this.mobileGUID = mobileGUID;
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

    // This check may be better refactored into a listener in persistence.xml - NC
    @Override
    protected void onCreate() {
        super.onCreate();
        if(sendEmailOnUpdate) {
            notification = new ProcedureNotification();
            notification.setProcedure(this);
            setNotification(notification);
        }
        ensureMobileGuidIsSet();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        ensureMobileGuidIsSet();
    }

    private void ensureMobileGuidIsSet() {
        if (mobileGUID == null) {
            mobileGUID = UUID.randomUUID().toString();
        }
    }

    @Transient
    public boolean hasLockResults() {
        return !getLockResults().isEmpty();
    }

    @Transient
    public boolean hasUnlockResults() {
        return !getUnlockResults().isEmpty();
    }

    public Date getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(Date unlockDate) {
        this.unlockDate = unlockDate;
    }

    public User getUnlockedBy() {
        return unlockedBy;
    }

    public void setUnlockedBy(User unlockedBy) {
        this.unlockedBy = unlockedBy;
    }

    @Transient
    public String getAssigneeName() {
        // Used as a path_expression in reporting for column event_search_assignee
        if (assignedGroup != null) {
            return assignedGroup.getName();
        } else if (assignee != null) {
            return assignee.getFullName();
        }
        return null;
    }

    public RecurringLotoEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringLotoEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }

    public ProcedureNotification getNotification() {
        return notification;
    }

    public void setNotification(ProcedureNotification notification) {
        this.notification = notification;
    }

    public Boolean isSendEmailOnUpdate() {
        return sendEmailOnUpdate;
    }

    public void setSendEmailOnUpdate(Boolean sendEmailOnUpdate) {
        this.sendEmailOnUpdate = sendEmailOnUpdate;
    }

    public Boolean isAssigneeOrDateChanged() {
        return assigneeOrDateChanged;
    }

    public void setAssigneeOrDateChanged() {
        this.assigneeOrDateChanged = Boolean.TRUE;
    }
}
