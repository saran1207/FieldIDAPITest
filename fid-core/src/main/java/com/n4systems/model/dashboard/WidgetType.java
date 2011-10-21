package com.n4systems.model.dashboard;

import com.n4systems.model.api.Listable;

public enum WidgetType implements Listable {

    SAMPLE("Sample"), 
    JOBS_ASSIGNED("Jobs Assigned"), 
    ASSETS_IDENTIFIED("Assets Identified"),
    COMMON_LINKS("Common Links"),
    NEWS("FieldId News");

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
