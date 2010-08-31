package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.util.ReportMap;

public class ProductStatusMapBuilderTest {
	
	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		ProductStatusBean pStatus = new ProductStatusBean();
		pStatus.setName("Product Status Name");
		
		ProductStatusMapBuilder builder = new ProductStatusMapBuilder();
		builder.addParams(reportMap, pStatus, null);
		
		assertEquals(pStatus.getName(), reportMap.get(ReportField.PRODUCT_STATUS.getParamKey()));
	}
	
}
