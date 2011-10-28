package com.n4systems.model.dashboard;

import com.google.common.base.CaseFormat;
import com.n4systems.model.api.Listable;

public enum WidgetType implements Listable {

	NEWS("FieldId News"),
    JOBS_ASSIGNED("Assigned Jobs"),
    COMPLETED_EVENTS("Completed Events"),
    ASSETS_IDENTIFIED("Assets Identified"),
    ASSETS_STATUS("Assets By Status"),
    COMMON_LINKS("Common Links"),
    UPCOMING_SCHEDULED_EVENTS("Upcoming Scheduled Events");

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

    // TODO DD : all widgets should be configurable?? 
    @Deprecated
    public boolean isConfigurable() {
        return true;
    }

    public String getCamelCase() { 
    	return  CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, toString());
    }

}
