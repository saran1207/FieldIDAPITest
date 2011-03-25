package com.n4systems.export.converters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ISODateConverter extends ExportValueConverter<Date> {
	private final SimpleDateFormat ISO8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return Date.class.isAssignableFrom(type);
	}

	@Override
	public String convertValue(Date obj) {
		return ISO8601Format.format(obj);
	}

}
