package com.n4systems.model.orders;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.LineItem;
import com.n4systems.model.Tenant;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.builders.TenantBuilder;

public class OrderFactoryTest {
	private OrderFactory orderFactory;
	private Tenant tenant;
	
	@Before
	public void setup_factory() {
		tenant = TenantBuilder.aTenant().build();
		orderFactory = new OrderFactory(tenant);
	}
	
	@Test
	public void create_non_integration_shop_order_returns_null_on_null_ordernumber() {
		assertNull(orderFactory.createNonIntegrationShopOrder(null));
	}
	
	@Test
	public void test_create_non_integration_shop_order() {
		String orderNumber = "ON12345";
		
		LineItem line = orderFactory.createNonIntegrationShopOrder(orderNumber);
		
		assertNotNull(line);
		assertNotNull(line.getOrder());
		assertEquals(tenant, line.getTenant());
		assertEquals(0, line.getIndex());
		assertEquals(OrderFactory.NON_INTEGRATION_DESCRIPTION, line.getDescription());
		
		assertEquals(tenant, line.getOrder().getTenant());
		assertEquals(orderNumber, line.getOrder().getOrderNumber());
		assertEquals(OrderType.SHOP, line.getOrder().getOrderType());
		assertEquals(OrderFactory.NON_INTEGRATION_DESCRIPTION, line.getOrder().getDescription());
	}
}
