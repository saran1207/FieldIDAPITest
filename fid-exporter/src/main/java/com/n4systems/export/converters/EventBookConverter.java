package com.n4systems.export.converters;

import com.n4systems.model.EventBook;

public class EventBookConverter extends ExportValueConverter<EventBook> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(EventBook.class);
	}

	@Override
	public String convertValue(EventBook book) {
		return book.getName();
	}

}
