package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.exceptions.NotImplementedException;
import com.n4systems.fieldid.api.pub.mapping.*;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage.AttributeMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AttributeValueType;
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
				.add(InfoFieldBean::getType, AttributeMessage.Builder::setType, AssetAttributeToMessage::convertInfoFieldType)
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

	private static Messages.AttributeValueType convertInfoFieldType(InfoFieldBean.InfoFieldType type, ConversionContext<InfoFieldBean, AttributeMessage.Builder> context) {
		switch (type) {
			case TextField:
				return AttributeValueType.TEXT;
			case SelectBox:
				return AttributeValueType.SELECT;
			case ComboBox:
				return AttributeValueType.COMBO;
			case UnitOfMeasure:
				return AttributeValueType.UNIT;
			case DateField:
				if (context.getFrom().isIncludeTime())
					return AttributeValueType.DATE_TIME;
				else
					return AttributeValueType.DATE;
			default:
				throw new NotImplementedException(type.toString());
		}
	}

	@Override
	public AttributeMessage convert(InfoOptionBean infoOptionBean) {
		AttributeMessage.Builder builder = AttributeMessage.newBuilder();
		map(infoOptionBean, builder);
		return builder.build();
	}
}
