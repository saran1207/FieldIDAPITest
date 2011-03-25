package com.n4systems.export.converters;

import com.n4systems.model.LineItem;

public class LineItemConverter extends ExportValueConverter<LineItem> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(LineItem.class);
	}

	@Override
	public String convertValue(LineItem lineItem) {
		return lineItem.getOrder().getOrderNumber();
	}

}
