package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

import java.util.Date;
import java.util.List;

public class ApiProcedure extends ApiReadWriteModel {

    private Date dueDate;
    private Long assigneeUserId;
    private Long assigneeUserGroupId;
    private String assetId;
    private String workflowState;
    private List<ApiIsolationPointResult> lockResults;

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
}
