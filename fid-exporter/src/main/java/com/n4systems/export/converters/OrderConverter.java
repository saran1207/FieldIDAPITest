package com.n4systems.export.converters;

import com.n4systems.model.Order;

public class OrderConverter extends ExportValueConverter<Order> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Order.class);
	}

	@Override
	public String convertValue(Order order) {
		return order.getOrderNumber();
	}

}
