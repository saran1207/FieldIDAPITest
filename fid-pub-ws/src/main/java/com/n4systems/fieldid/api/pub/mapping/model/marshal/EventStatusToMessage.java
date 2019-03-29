package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.EventStatus;

public class EventStatusToMessage<T> extends TypeMapper<EventStatus, T> {

	public EventStatusToMessage(SetterReference<T, String> idSetter, SetterReference<T, String> nameSetter) {
		super(TypeMapperBuilder.<EventStatus, T>newBuilder()
				.add(EventStatus::getPublicId, idSetter, false)
				.add(EventStatus::getName, nameSetter, false)
				.build());
	}
}
