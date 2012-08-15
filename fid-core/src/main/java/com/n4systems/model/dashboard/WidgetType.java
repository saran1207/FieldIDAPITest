package com.n4systems.model.dashboard;

import com.google.common.base.CaseFormat;
import com.n4systems.model.api.Listable;
import com.n4systems.model.dashboard.widget.*;

public enum WidgetType implements Listable<String> {

	NEWS("Field ID News & Updates", WidgetConfiguration.class),
    JOBS_ASSIGNED("Assigned Jobs", WidgetConfiguration.class),
    COMMON_LINKS("Common Links", WidgetConfiguration.class),
    COMPLETED_EVENTS("Completed Events", CompletedEventsWidgetConfiguration.class),
    ASSETS_STATUS("Assets By Status", AssetsStatusWidgetConfiguration.class),
    UPCOMING_SCHEDULED_EVENTS("Upcoming Scheduled Events", UpcomingEventsWidgetConfiguration.class),
    ASSETS_IDENTIFIED("Assets Identified", AssetsIdentifiedWidgetConfiguration.class),
    EVENT_COMPLETENESS("Event Completeness", EventCompletenessWidgetConfiguration.class),
    WORK("Work", WorkWidgetConfiguration.class),
    EVENT_KPI("Event KPIs", EventKPIWidgetConfiguration.class);

    private String description;
    private Class<? extends WidgetConfiguration> configurationClass;

    WidgetType(String description, Class<? extends WidgetConfiguration> configurationClass) {
        this.description = description;
        this.configurationClass = configurationClass;
    }

    @Override
    public String getId() {
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
    	return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, toString());
    }

}
