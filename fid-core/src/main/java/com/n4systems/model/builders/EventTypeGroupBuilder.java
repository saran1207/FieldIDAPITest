package com.n4systems.model.builders;

import com.n4systems.model.EventTypeGroup;

public class EventTypeGroupBuilder extends BaseBuilder<EventTypeGroup> {

	private final String name;

	public static EventTypeGroupBuilder anInspectionTypeGroup() {
		return new EventTypeGroupBuilder("some name");
	}

	private EventTypeGroupBuilder(String name) {
		this.name = name;
	}

	public EventTypeGroupBuilder withName(String name) {
		return new EventTypeGroupBuilder(name);
	}
	
	@Override
	public EventTypeGroup createObject() {
		EventTypeGroup eventTypeGroup = new EventTypeGroup();
		eventTypeGroup.setId(generateNewId());
		eventTypeGroup.setName(name);
		
		return eventTypeGroup;
	}
}
