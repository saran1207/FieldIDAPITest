package com.n4systems.model.orders;

import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Tenant;
import com.n4systems.model.Order.OrderType;
import com.n4systems.util.StringUtils;

public class OrderFactory {
	// XXX - I'm not exactly sure why the description is set to an empty string.  This
	// logic comes directly from the OrderManagerImpl's call for createNonIntegrationShopOrder
	protected static final String NON_INTEGRATION_DESCRIPTION = "";
	
	protected final Tenant tenant;
	
	public OrderFactory(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public LineItem createNonIntegrationShopOrder(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}

		Order order = createNonItegrationOrder(orderNumber);
		LineItem line = createNonItegrationLineItem(order);
		return line;
	}
	
	protected Order createNonItegrationOrder(String orderNumber) {
		Order order = new Order(OrderType.SHOP, orderNumber);
		order.setTenant(tenant);
		order.setDescription(NON_INTEGRATION_DESCRIPTION);
		return order;
	}
	
	protected LineItem createNonItegrationLineItem(Order order) {
		LineItem lineItem = new LineItem(order);
		lineItem.setTenant(tenant);
		lineItem.setIndex(0);
		lineItem.setDescription(NON_INTEGRATION_DESCRIPTION);
		return lineItem;
	}
	
}
