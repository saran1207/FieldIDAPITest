package com.n4systems.model.search;

import com.n4systems.model.user.User;

public interface PeopleCriteria {

    User getAssignee();
    void setAssignee(User assignee);

    boolean isUnassignedOnly();
    void setUnassignedOnly(boolean unassignedOnly);

    public User getPerformedBy();
    public void setPerformedBy(User performedBy);

}
