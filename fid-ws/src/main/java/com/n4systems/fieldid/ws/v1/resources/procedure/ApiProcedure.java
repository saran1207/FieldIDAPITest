package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

import java.util.Date;

public class ApiProcedure extends ApiReadWriteModel {

    private Date dueDate;
    private Date completedDate;
    private Long assigneeUserId;
    private Long assigneeUserGroupId;
    private String assetId;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
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
}
