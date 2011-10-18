package com.n4systems.model.dashboard;

import com.n4systems.model.api.Listable;

public enum WidgetType implements Listable {

    SAMPLE("Sample"), JOBS_ASSIGNED("Jobs Assigned");

    private String description;

    WidgetType(String description) {
        this.description = description;
    }

    @Override
    public Object getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return description;
    }
}
