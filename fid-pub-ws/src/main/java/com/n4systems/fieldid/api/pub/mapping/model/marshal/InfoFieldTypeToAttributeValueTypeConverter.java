package com.n4systems.fieldid.api.pub.mapping.model.marshal;


import com.n4systems.fieldid.api.pub.exceptions.NotImplementedException;
import com.n4systems.fieldid.api.pub.mapping.ConversionContext;
import com.n4systems.fieldid.api.pub.mapping.ValueConverterWithContext;
import com.n4systems.fieldid.api.pub.model.Messages.AttributeValueType;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoFieldBean.InfoFieldType;

public class InfoFieldTypeToAttributeValueTypeConverter<ToType> implements ValueConverterWithContext<InfoFieldType, AttributeValueType, InfoFieldBean, ToType> {

	public AttributeValueType convert(InfoFieldType type, ConversionContext<InfoFieldBean, ToType> context) {
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
}
