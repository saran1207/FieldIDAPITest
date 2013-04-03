package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

import java.util.Date;

public class ApiProcedure extends ApiReadonlyModel {

    private Date dueDate;
    private Date completedDate;
    private Long assigneeUserId;
    private Long assigneeUserGroupId;

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
}
