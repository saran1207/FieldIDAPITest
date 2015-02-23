package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.mapping.Converter;
import com.n4systems.fieldid.api.pub.mapping.GetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.GpsLocation;

public class MessageToGpsLocation<T> extends TypeMapper<T, GpsLocation> {

	public MessageToGpsLocation(GetterReference<T, String> getLatitude, GetterReference<T, String> getLongitude) {
		super(TypeMapperBuilder.<T, GpsLocation>newBuilder()
				.add(getLatitude, GpsLocation::setLatitude, Converter.convertToBigDecimal(), true)
				.add(getLongitude, GpsLocation::setLongitude, Converter.convertToBigDecimal(), true)
				.build());
	}
}
