package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.AssociatedEventTypeMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.AssociatedEventTypeMessage.Builder;
import com.n4systems.model.AssociatedEventType;

public class AssociatedEventTypeToMessage  extends TypeMapper<AssociatedEventType, Builder> implements ValueConverter<AssociatedEventType, AssociatedEventTypeMessage> {

	public AssociatedEventTypeToMessage() {
		super(TypeMapperBuilder.<AssociatedEventType, Builder>newBuilder()
				.addModelToMessage(AssociatedEventType::getEventType, new ApiModelWithNameToMessage<>(Builder::setEventTypeId, Builder::setEventTypeName))
				.build());
	}

	@Override
	public AssociatedEventTypeMessage convert(AssociatedEventType value) {
		Builder builder = AssociatedEventTypeMessage.newBuilder();
		map(value, builder);
		return builder.build();
	}

}