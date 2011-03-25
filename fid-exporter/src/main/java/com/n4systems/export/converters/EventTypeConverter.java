package com.n4systems.export.converters;

import com.n4systems.model.EventType;

public class EventTypeConverter extends ExportValueConverter<EventType> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(EventType.class);
	}

	@Override
	public String convertValue(EventType type) {
		return type.getName();
	}

}
