package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.PriorityCode;

public class PriorityCodeToMessage<T> extends TypeMapper<PriorityCode, T> {

	public PriorityCodeToMessage(SetterReference<T, String> idSetter, SetterReference<T, String> nameSetter) {
		super(TypeMapperBuilder.<PriorityCode, T>newBuilder()
				.add(PriorityCode::getPublicId, idSetter, false)
				.add(PriorityCode::getName, nameSetter, false)
				.build());
	}
}
