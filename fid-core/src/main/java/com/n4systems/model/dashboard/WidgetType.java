package com.n4systems.model.dashboard;

import com.n4systems.model.api.Listable;

public enum WidgetType implements Listable {

	NEWS("FieldId News", false),
    JOBS_ASSIGNED("Assigned Jobs", false),
    ASSETS_IDENTIFIED("Assets Identified", true),
    ASSETS_STATUS("Assets By Status", true),
    COMMON_LINKS("Common Links", false),
    UPCOMING_SCHEDULED_EVENTS("Upcoming Scheduled Events", true);

    private String description;
    private boolean configurable;

    WidgetType(String description, boolean configurable) {
        this.description = description;
        this.configurable = configurable;
    }

    @Override
    public Object getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return description;
    }

    public boolean isConfigurable() {
        return configurable;
    }

}
