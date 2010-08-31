package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.ProductType;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.util.ReportMap;

public class ProductTypeMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> params = new ReportMap<Object>();
		
		ProductType type = ProductTypeBuilder.aProductType().build();
		
		ProductTypeMapBuilder builder = new ProductTypeMapBuilder();
		builder.addParams(params, type, null);
		
		assertEquals(type.getName(), params.get(ReportField.PRODUCT_TYPE_NAME.getParamKey()));
		assertEquals(type.getName(), params.get(ReportField.PRODUCT_TYPE_NAME_LEGACY.getParamKey()));
		assertEquals(type.getWarnings(), params.get(ReportField.PRODUCT_TYPE_WARNING.getParamKey()));
		assertEquals(type.getManufactureCertificateText(), params.get(ReportField.PRODUCT_TYPE_CERT_TEXT.getParamKey()));
		
	}
	
}
