package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.GpsLocation;

public class GpsLocationToMessage<T> extends TypeMapper<GpsLocation, T> {

	public GpsLocationToMessage(SetterReference<T, String> setLatitude, SetterReference<T, String> setLongitude) {
		super(TypeMapperBuilder.<GpsLocation, T>newBuilder()
				.addToString(GpsLocation::getLatitude, setLatitude)
				.addToString(GpsLocation::getLongitude, setLongitude)
				.build());
	}
}
