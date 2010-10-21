package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;

public class LineItemMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		Transaction transaction = new DummyTransaction();
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		LineItem lineItem = new LineItem();
		lineItem.setDescription(TestHelper.randomString());
		lineItem.setAssetCode(TestHelper.randomString());
		lineItem.setOrder(new Order());
		
		MapBuilder<Order> orderBuilder = EasyMock.createMock(MapBuilder.class);
		orderBuilder.addParams(reportMap, lineItem.getOrder(), transaction);
		EasyMock.replay(orderBuilder);
		
		LineItemMapBuilder builder = new LineItemMapBuilder(orderBuilder);
		builder.addParams(reportMap, lineItem, transaction);
		
		assertEquals(lineItem.getDescription(), reportMap.get(ReportField.LINE_ITEM_DESC.getParamKey()));
		assertEquals(lineItem.getAssetCode(), reportMap.get(ReportField.LINE_ITEM_PRODUCT_CODE.getParamKey()));
		
	}
}
