package com.n4systems.model.orders;

import com.n4systems.model.LineItem;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;

public class NonIntegrationOrderManager {
	private final NonIntegrationLineItemSaver lineSaver;
	
	public NonIntegrationOrderManager(NonIntegrationLineItemSaver lineSaver) {
		this.lineSaver = lineSaver;
	}
	
	public LineItem createAndSave(String orderNumber, Tenant tenant, Transaction transaction) {
		LineItem line = createOrderFactory(tenant).createNonIntegrationShopOrder(orderNumber);
		lineSaver.save(transaction, line);
		return line;
	}
	
	protected OrderFactory createOrderFactory(Tenant tenant) {
		return new OrderFactory(tenant);
	}
}
