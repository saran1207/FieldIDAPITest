package com.n4systems.model.orders;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.LineItem;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.TestingTransaction;

public class NonIntegrationOrderManagerTest {
	private static final String testOrderNumber = "ON123";
	
	@Test
	public void test_save_and_create() {
		final Tenant tenant = new Tenant();
		final LineItem line = new LineItem();
		final Transaction transaction = new TestingTransaction();

		NonIntegrationLineItemSaver saver = createMock(NonIntegrationLineItemSaver.class);
		
		NonIntegrationOrderManager manager = new NonIntegrationOrderManager(saver) {
			@Override
			protected OrderFactory createOrderFactory(Tenant tenant) {
				return new OrderFactory(tenant) {
					@Override
					public LineItem createNonIntegrationShopOrder(String orderNumber) {
						assertEquals(testOrderNumber, orderNumber);
						assertSame(tenant, this.tenant);
						return line;
					}			
				};
			}
		};
		
		saver.save(transaction, line);
		replay(saver);
		
		LineItem createdLine = manager.createAndSave(testOrderNumber, tenant, transaction);
		verify(saver);
		
		assertEquals(line, createdLine);
	}
}
