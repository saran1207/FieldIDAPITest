package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.mapping.GetterReference;
import com.n4systems.fieldid.api.pub.mapping.HasserReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.location.Location;

public class MessageToLocation<T> extends TypeMapper<T, Location> {

	public MessageToLocation(HasserReference<T> hasFreeformLocation, GetterReference<T, String> getFreeformLocation, HasserReference<T> hasPredefinedLocationId, GetterReference<T, String> getPredefinedLocationId, PredefinedLocationResolver predefinedLocationResolver) {
		super(TypeMapperBuilder.<T, Location>newBuilder()
				.add(hasFreeformLocation, getFreeformLocation, Location::setFreeformLocation, true)
				.add(hasPredefinedLocationId, getPredefinedLocationId, Location::setPredefinedLocation, predefinedLocationResolver, true)
				.build());
	}
}
