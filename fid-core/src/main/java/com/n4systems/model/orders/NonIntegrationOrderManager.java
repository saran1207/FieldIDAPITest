package com.n4systems.model.orders;

import com.n4systems.model.LineItem;
import com.n4systems.model.Tenant;

public class NonIntegrationOrderManager {
	private final NonIntegrationLineItemSaver lineSaver;
	
	public NonIntegrationOrderManager(NonIntegrationLineItemSaver lineSaver) {
		this.lineSaver = lineSaver;
	}
	
	public LineItem createAndSave(String orderNumber, Tenant tenant) {
		LineItem line = createOrderFactory(tenant).createNonIntegrationShopOrder(orderNumber);
		lineSaver.save(line);
		return line;
	}
	
	protected OrderFactory createOrderFactory(Tenant tenant) {
		return new OrderFactory(tenant);
	}
}
