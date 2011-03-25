package com.n4systems.export.converters;

import com.n4systems.model.location.Location;

public class LocationConverter extends ExportValueConverter<Location> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Location.class);
	}

	@Override
	public String convertValue(Location location) {
		return location.getFullName();
	}

}
