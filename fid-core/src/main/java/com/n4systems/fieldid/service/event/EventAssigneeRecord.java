package com.n4systems.fieldid.service.event;

import com.n4systems.model.user.User;

public class EventAssigneeRecord {

    private User assignee;
    private Long count;

    public EventAssigneeRecord(User assignee, Long count) {
        this.assignee = assignee;

        this.count = count;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
