package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeScheduleMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeScheduleMessage.Builder;
import com.n4systems.model.AssetTypeSchedule;

public class AssetTypeScheduleToMessage extends TypeMapper<AssetTypeSchedule, Builder> implements ValueConverter<AssetTypeSchedule, AssetTypeScheduleMessage> {

	public AssetTypeScheduleToMessage() {
		super(TypeMapperBuilder.<AssetTypeSchedule, Builder>newBuilder()
				.add(AssetTypeSchedule::getPublicId, Builder::setId)
				.addDateToString(AssetTypeSchedule::getCreated, Builder::setCreatedDate)
				.addDateToString(AssetTypeSchedule::getModified, Builder::setModifiedDate)
				.addModelToMessage(AssetTypeSchedule::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(AssetTypeSchedule::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addModelToMessage(AssetTypeSchedule::getOwner, new BaseOrgToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
				.addModelToMessage(AssetTypeSchedule::getAssetType, new ApiModelWithNameToMessage<>(Builder::setAssetTypeId, Builder::setAssetTypeName))
				.addModelToMessage(AssetTypeSchedule::getEventType, new ApiModelWithNameToMessage<>(Builder::setEventTypeId, Builder::setEventTypeName))
				.add(AssetTypeSchedule::getFrequency, Builder::setFrequencyInDays)
				.add(AssetTypeSchedule::isAutoSchedule, Builder::setAutoSchedule)
				.build());
	}

	@Override
	public AssetTypeScheduleMessage convert(AssetTypeSchedule value) {
		Builder builder = AssetTypeScheduleMessage.newBuilder();
		map(value, builder);
		return builder.build();
	}

}
