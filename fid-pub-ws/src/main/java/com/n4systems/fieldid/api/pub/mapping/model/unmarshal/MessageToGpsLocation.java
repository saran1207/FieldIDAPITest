package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.mapping.*;
import com.n4systems.model.GpsLocation;

public class MessageToGpsLocation<T> extends TypeMapper<T, GpsLocation> {

	public MessageToGpsLocation(HasserReference<T> hasLatitude, GetterReference<T, String> getLatitude, HasserReference<T> hasLongitude, GetterReference<T, String> getLongitude) {
		super(TypeMapperBuilder.<T, GpsLocation>newBuilder()
				.add(hasLatitude, getLatitude, GpsLocation::setLatitude, Converter.convertToBigDecimal(), true)
				.add(hasLongitude, getLongitude, GpsLocation::setLongitude, Converter.convertToBigDecimal(), true)
				.build());
	}
}
