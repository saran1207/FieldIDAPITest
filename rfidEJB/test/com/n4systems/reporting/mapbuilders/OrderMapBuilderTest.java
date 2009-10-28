package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Order;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;

public class OrderMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		OrderMapBuilder builder = new OrderMapBuilder();
		
		Order order = new Order();
		order.setOrderNumber(TestHelper.randomString());
		
		builder.addParams(reportMap, order, null);
		
		assertEquals(order.getOrderNumber(), reportMap.get(ReportField.ORDER_NUMBER.getParamKey()));
	}
	
}
