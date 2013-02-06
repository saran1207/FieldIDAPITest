package com.n4systems.model.dashboard;

import com.google.common.base.CaseFormat;
import com.n4systems.model.api.Listable;
import com.n4systems.model.dashboard.widget.*;

public enum WidgetType implements Listable<String> {

	NEWS("Field ID News & Updates", "Latest posts from the FieldID Customer Blog", WidgetConfiguration.class),
    JOBS_ASSIGNED("Assigned Jobs", "A list of all open jobs you are assigned to",  WidgetConfiguration.class),
    COMPLETED_EVENTS("Completed Events", "A graph of all completed events by result", CompletedEventsWidgetConfiguration.class),
    ASSETS_STATUS("Assets By Status", "A bar graph showing assets by their status", AssetsStatusWidgetConfiguration.class),
    UPCOMING_SCHEDULED_EVENTS("Upcoming Scheduled Events", "Scheduled events for the next 30,60 or 90 days", UpcomingEventsWidgetConfiguration.class),
    ASSETS_IDENTIFIED("Assets Identified", "Graph of the number of assets identified", AssetsIdentifiedWidgetConfiguration.class),
    EVENT_COMPLETENESS("Event Completeness", "Number of open vs completed events", EventCompletenessWidgetConfiguration.class),
    WORK("Work", "A calendar view of work assigned to you", WorkWidgetConfiguration.class),
    ACTIONS("Open Actions by Priority", "A list of open actions graphed by priority", ActionsWidgetConfiguration.class),
    EVENT_KPI("Event KPIs", "Compare completed events across multiple locations", EventKPIWidgetConfiguration.class);

    private String name;
    private String description;
    private Class<? extends WidgetConfiguration> configurationClass;

    WidgetType(String name, String description, Class<? extends WidgetConfiguration> configurationClass) {
        this.name = name;
        this.description = description;
        this.configurationClass = configurationClass;
    }

    @Override
    public String getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public WidgetConfiguration createConfiguration() {
        try {
            WidgetConfiguration widgetConfiguration = configurationClass.newInstance();
            widgetConfiguration.setName(name);
            return widgetConfiguration;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create instance of configuration class: " + configurationClass);
        }
    }

    public String getCamelCase() { 
    	return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, toString());
    }

}
