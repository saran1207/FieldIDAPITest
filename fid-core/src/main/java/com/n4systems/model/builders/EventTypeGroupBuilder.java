package com.n4systems.model.builders;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Tenant;

public class EventTypeGroupBuilder extends BaseBuilder<EventTypeGroup> {

	private final String name;
    private final String reportTitle;
    private Tenant tenant;

	public static EventTypeGroupBuilder anEventTypeGroup() {
		return new EventTypeGroupBuilder("some name", "reportTitle", null);
	}

	private EventTypeGroupBuilder(String name, String reportTitle, Tenant tenant) {
		this.name = name;
        this.reportTitle = reportTitle;
        this.tenant = tenant;
	}

	public EventTypeGroupBuilder withName(String name) {
		return makeBuilder(new EventTypeGroupBuilder(name, reportTitle, tenant));
	}

	public EventTypeGroupBuilder withReportTitle(String reportTitle) {
		return makeBuilder(new EventTypeGroupBuilder(name, reportTitle, tenant));
	}
	
	public EventTypeGroupBuilder forTenant(Tenant tenant) {
		return makeBuilder(new EventTypeGroupBuilder(name, reportTitle, tenant));
	}
	
	@Override
	public EventTypeGroup createObject() {
		EventTypeGroup eventTypeGroup = new EventTypeGroup();
		eventTypeGroup.setId(getId());
		eventTypeGroup.setName(name);
        eventTypeGroup.setReportTitle(reportTitle);
        eventTypeGroup.setTenant(tenant);
		
		return eventTypeGroup;
	}
}
