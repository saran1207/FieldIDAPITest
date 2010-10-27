package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.util.ReportMap;

public class ProductStatusMapBuilderTest {
	
	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		AssetStatus pStatus = new AssetStatus();
		pStatus.setName("Asset Status Name");
		
		AssetStatusMapBuilder builder = new AssetStatusMapBuilder();
		builder.addParams(reportMap, pStatus, null);
		
		assertEquals(pStatus.getName(), reportMap.get(ReportField.PRODUCT_STATUS.getParamKey()));
	}
	
}
