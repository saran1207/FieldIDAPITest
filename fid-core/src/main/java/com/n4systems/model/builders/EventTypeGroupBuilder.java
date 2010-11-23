package com.n4systems.model.builders;

import com.n4systems.model.EventTypeGroup;

public class EventTypeGroupBuilder extends BaseBuilder<EventTypeGroup> {

	private final String name;
    private final String reportTitle;

	public static EventTypeGroupBuilder anEventTypeGroup() {
		return new EventTypeGroupBuilder("some name", "reportTitle");
	}

	private EventTypeGroupBuilder(String name, String reportTitle) {
		this.name = name;
        this.reportTitle = reportTitle;
	}

	public EventTypeGroupBuilder withName(String name) {
		return makeBuilder(new EventTypeGroupBuilder(name, reportTitle));
	}

	public EventTypeGroupBuilder reportTitle(String reportTitle) {
		return makeBuilder(new EventTypeGroupBuilder(name, reportTitle));
	}
	
	@Override
	public EventTypeGroup createObject() {
		EventTypeGroup eventTypeGroup = new EventTypeGroup();
		eventTypeGroup.setId(getId());
		eventTypeGroup.setName(name);
        eventTypeGroup.setReportTitle(reportTitle);
		
		return eventTypeGroup;
	}
}
