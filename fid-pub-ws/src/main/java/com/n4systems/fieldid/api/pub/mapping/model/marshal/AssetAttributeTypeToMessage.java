package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.AssetAttributeTypeMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.AssetAttributeTypeMessage.Builder;
import rfid.ejb.entity.InfoFieldBean;

import java.util.stream.Collectors;

public class AssetAttributeTypeToMessage extends TypeMapper<InfoFieldBean, Builder> implements ValueConverter<InfoFieldBean, AssetAttributeTypeMessage> {

	public AssetAttributeTypeToMessage() {
		super(TypeMapperBuilder.<InfoFieldBean, Builder>newBuilder()
				.add(InfoFieldBean::getPublicId, Builder::setId)
				.add(InfoFieldBean::getName, Builder::setName)
				.add(InfoFieldBean::getType, Builder::setType, new InfoFieldTypeToAttributeValueTypeConverter<>())
				.addCollection(InfoFieldBean::getInfoOptions, Builder::addAllAttributeOptions, (field) -> field.getName(), Collectors.toList())
				.build());
	}

	@Override
	public AssetAttributeTypeMessage convert(InfoFieldBean value) {
		Builder builder = AssetAttributeTypeMessage.newBuilder();
		map(value, builder);
		return builder.build();
	}

}
