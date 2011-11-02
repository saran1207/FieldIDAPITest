package com.n4systems.model.dashboard;

import com.google.common.base.CaseFormat;
import com.n4systems.model.api.Listable;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

public enum WidgetType implements Listable {

	NEWS("FieldId News", WidgetConfiguration.class),
    JOBS_ASSIGNED("Assigned Jobs", WidgetConfiguration.class),
    COMPLETED_EVENTS("Completed Events", WidgetConfiguration.class),
    ASSETS_IDENTIFIED("Assets Identified", AssetsIdentifiedWidgetConfiguration.class),
    ASSETS_STATUS("Assets By Status", WidgetConfiguration.class),
    COMMON_LINKS("Common Links", WidgetConfiguration.class),
    UPCOMING_SCHEDULED_EVENTS("Upcoming Scheduled Events", WidgetConfiguration.class),
    EVENT_KPI("Event KPIs", EventKPIWidgetConfiguration.class);

    private String description;
    private Class<? extends WidgetConfiguration> configurationClass;

    WidgetType(String description, Class<? extends WidgetConfiguration> configurationClass) {
        this.description = description;
        this.configurationClass = configurationClass;
    }

    @Override
    public Object getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return description;
    }

    public WidgetConfiguration createConfiguration() {
        try {
            WidgetConfiguration widgetConfiguration = configurationClass.newInstance();
            widgetConfiguration.setName(description);
            return widgetConfiguration;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create instance of configuration class: " + configurationClass);
        }
    }

    public String getCamelCase() { 
    	return  CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, toString());
    }

}
