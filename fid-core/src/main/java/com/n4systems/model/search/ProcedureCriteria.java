package com.n4systems.model.search;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;

import java.util.List;

public class ProcedureCriteria extends SearchCriteria implements PeopleCriteria {

    private AssetType assetType;

    private AssetTypeGroup assetTypeGroup;

    private User assignee;

    private UserGroup assignedUserGroup;

    private boolean unassignedOnly;

    private DateRange dateRange = new DateRange(RangeType.CUSTOM);

    private DateRange dueDateRange = new DateRange(RangeType.CUSTOM);

    private AssetStatus assetStatus;

    private String rfidNumber;

    private String identifier;

    private String referenceNumber;

    private String orderNumber;

    private String purchaseOrder;

    private User performedBy;

    private ProcedureWorkflowStateCriteria workflowState = ProcedureWorkflowStateCriteria.OPEN;

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

    public ProcedureWorkflowStateCriteria getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(ProcedureWorkflowStateCriteria workflowStateCriteria) {
        this.workflowState = workflowStateCriteria;
    }

    public DateRange getDueDateRange() {
        return dueDateRange;
    }

    public void setDueDateRange(DateRange dueDateRange) {
        this.dueDateRange = dueDateRange;
    }

    public void clearDateRanges() {
        this.dateRange = new DateRange(RangeType.CUSTOM);
        this.dueDateRange = new DateRange(RangeType.CUSTOM);
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignedUserGroup = null;
        this.assignee = assignee;
    }

    public UserGroup getAssignedUserGroup() {
        return assignedUserGroup;
    }

    public void setAssignedUserGroup(UserGroup assignedUserGroup) {
        this.assignee = null;
        this.assignedUserGroup = assignedUserGroup;
    }

    public boolean isUnassignedOnly() {
        return unassignedOnly;
    }

    public void setUnassignedOnly(boolean unassignedOnly) {
        this.unassignedOnly = unassignedOnly;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    @Override
    public List<String> getColumns() {
        System.out.println("get colunns");
        return null;
    }

    @Override
    public void setColumns(List<String> columns) {
        System.out.println("set column");
    }

    public AssetTypeGroup getAssetTypeGroup() {
        return assetTypeGroup;
    }

    public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        this.assetTypeGroup = assetTypeGroup;
    }

    public void setAssignedUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setUnassignedOnly(false);
            setAssignee((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setUnassignedOnly(false);
            setAssignedUserGroup((UserGroup) assignee);
        } else if (assignee == UnassignedIndicator.UNASSIGNED) {
            setUnassignedOnly(true);
            setAssignee(null);
            setAssignedUserGroup(null);
        } else if (assignee == null) {
            setUnassignedOnly(false);
            setAssignee(null);
            setAssignedUserGroup(null);
        }
    }

    public Assignable getAssignedUserOrGroup() {
        if (getAssignee() != null) {
            return getAssignee();
        }
        return getAssignedUserGroup();
    }

}
