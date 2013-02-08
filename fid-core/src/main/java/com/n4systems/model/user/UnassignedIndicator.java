package com.n4systems.model.user;

import java.io.Serializable;

public class UnassignedIndicator implements CanHaveEventsAssigned, Serializable {

    public static UnassignedIndicator UNASSIGNED = new UnassignedIndicator();

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getDisplayName() {
        return "";
    }

}
