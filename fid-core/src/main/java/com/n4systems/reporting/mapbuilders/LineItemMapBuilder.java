package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.persistence.Transaction;

public class LineItemMapBuilder extends AbstractMapBuilder<LineItem> {
	private final MapBuilder<Order> orderMapBuilder;
	
	public LineItemMapBuilder(MapBuilder<Order> orderMapBuilder) {
		super(ReportField.LINE_ITEM_DESC, ReportField.LINE_ITEM_PRODUCT_CODE);
		
		this.orderMapBuilder = orderMapBuilder;
	}
	
	public LineItemMapBuilder() {
		this(new OrderMapBuilder());
	}
	
	@Override
	protected void setAllFields(LineItem entity, Transaction transaction) {
		setField(ReportField.LINE_ITEM_DESC, 			entity.getDescription());
		setField(ReportField.LINE_ITEM_PRODUCT_CODE,	entity.getAssetCode());
		
		setAllFields(orderMapBuilder, entity.getOrder(), transaction);
	}

}
