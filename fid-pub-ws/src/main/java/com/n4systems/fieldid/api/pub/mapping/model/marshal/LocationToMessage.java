package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;

public class LocationToMessage<T> extends TypeMapper<Location, T> {

	public LocationToMessage(SetterReference<T, String> setFreeformLocation, SetterReference<T, String> setPredefinedLocationId, SetterReference<T, String> setPredefinedLocationName) {
		super(TypeMapperBuilder.<Location, T>newBuilder()
				.add(Location::getFreeformLocation, setFreeformLocation)
				.addModelToMessage(Location::getPredefinedLocation, (builder) -> builder
						.add(PredefinedLocation::getPublicId, setPredefinedLocationId)
						.add(PredefinedLocation::getName, setPredefinedLocationName)
						.build())
				.build());
	}
}
