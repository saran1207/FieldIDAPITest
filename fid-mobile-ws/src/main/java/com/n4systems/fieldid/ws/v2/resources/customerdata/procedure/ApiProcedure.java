package com.n4systems.fieldid.ws.v2.resources.customerdata.procedure;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;

import java.util.Date;
import java.util.List;

public class ApiProcedure extends ApiReadWriteModel {
    private Date dueDate;
    private Long assigneeUserId;
    private Long assigneeUserGroupId;
    private String assetId;
    private String assetIdentifier;
    private String workflowState;
    private List<ApiIsolationPointResult> lockResults;
    private String procedureDefinitionId;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAssigneeUserId() {
        return assigneeUserId;
    }

    public void setAssigneeUserId(Long assigneeUserId) {
        this.assigneeUserId = assigneeUserId;
    }

    public Long getAssigneeUserGroupId() {
        return assigneeUserGroupId;
    }

    public void setAssigneeUserGroupId(Long assigneeUserGroupId) {
        this.assigneeUserGroupId = assigneeUserGroupId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(String workflowState) {
        this.workflowState = workflowState;
    }

    public List<ApiIsolationPointResult> getLockResults() {
        return lockResults;
    }

    public void setLockResults(List<ApiIsolationPointResult> lockResults) {
        this.lockResults = lockResults;
    }

    public String getAssetIdentifier() {
        return assetIdentifier;
    }

    public void setAssetIdentifier(String assetIdentifier) {
        this.assetIdentifier = assetIdentifier;
    }

    public String getProcedureDefinitionId() {
        return procedureDefinitionId;
    }

    public void setProcedureDefinitionId(String procedureDefinitionId) {
        this.procedureDefinitionId = procedureDefinitionId;
    }
}
