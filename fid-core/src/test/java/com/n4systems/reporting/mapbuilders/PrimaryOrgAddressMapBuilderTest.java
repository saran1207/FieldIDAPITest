package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.util.ReportMap;

public class PrimaryOrgAddressMapBuilderTest {

	@Test
	public void testSetAllFields() {
		AddressInfo address = AddressInfoBuilder.anAddress().build();
		
		PrimaryOrgAddressMapBuilder builder = new PrimaryOrgAddressMapBuilder();

		ReportMap<Object> params = new ReportMap<Object>();
		builder.addParams(params, address, null);
		
		assertEquals(address.getDisplay(), params.get(ReportField.PRIMARY_ORG_ADDRESS.getParamKey()));
	}
	
}
