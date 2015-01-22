package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.*;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage.AttributeMessage;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.Date;

public class AssetAttributeToMessage extends TypeMapper<InfoOptionBean, AttributeMessage.Builder> implements ValueConverter<InfoOptionBean, AttributeMessage> {

	public AssetAttributeToMessage() {
		super(TypeMapperBuilder.<InfoOptionBean, AttributeMessage.Builder>newBuilder()
				.add(InfoOptionBean::getName, AttributeMessage.Builder::setValue, AssetAttributeToMessage::convertInfoOptionValue)
				.addModelToMessage(InfoOptionBean::getInfoField, getInfoFieldTypeMapper())
				.build());
	}

	private static Mapper<InfoFieldBean, AttributeMessage.Builder> getInfoFieldTypeMapper() {
		return TypeMapperBuilder.<InfoFieldBean, AttributeMessage.Builder>newBuilder()
				.add(InfoFieldBean::getPublicId, AttributeMessage.Builder::setId)
				.add(InfoFieldBean::getName, AttributeMessage.Builder::setName)
				.add(InfoFieldBean::getType, AttributeMessage.Builder::setType, new InfoFieldTypeToAttributeValueTypeConverter<>())
				.build();
	}

	private static String convertInfoOptionValue(String value, ConversionContext<InfoOptionBean, AttributeMessage.Builder> context) {
		InfoOptionBean infoOption = context.getFrom();
		if (infoOption.getInfoField().isDateField()) {
			Date date = new Date(Long.parseLong(infoOption.getName()));
			if (infoOption.getInfoField().isIncludeTime()) {
				return Converter.convertDateTimeToString().convert(date);
			} else {
				return Converter.convertDateToString().convert(date);
			}
		} else {
			return value;
		}
	}

	@Override
	public AttributeMessage convert(InfoOptionBean infoOptionBean) {
		AttributeMessage.Builder builder = AttributeMessage.newBuilder();
		map(infoOptionBean, builder);
		return builder.build();
	}
}
