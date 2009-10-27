package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.Order;
import com.n4systems.persistence.Transaction;

public class OrderMapBuilder extends AbstractMapBuilder<Order> {

	public OrderMapBuilder() {
		super(ReportField.ORDER_NUMBER);
	}
	
	@Override
	protected void setAllFields(Order entity, Transaction transaction) {
		setField(ReportField.ORDER_NUMBER, entity);
	}

}
