package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.util.ReportMap;

public class CustomerAddressMapBuilderTest {

	@Test
	public void testSetAllFields() {
		AddressInfo address = AddressInfoBuilder.anAddress().build();
		
		CustomerAddressMapBuilder builder = new CustomerAddressMapBuilder();

		ReportMap<Object> params = new ReportMap<Object>();
		builder.addParams(params, address, null);
		
		assertSame(address.getStreetAddress(), params.get(ReportField.CUSTOMER_ADDRESS_STREET.getParamKey()));
		assertSame(address.getCity(), params.get(ReportField.CUSTOMER_ADDRESS_CITY.getParamKey()));
		assertSame(address.getState(), params.get(ReportField.CUSTOMER_ADDRESS_STATE.getParamKey()));
		assertSame(address.getZip(), params.get(ReportField.CUSTOMER_ADDRESS_POSTAL.getParamKey()));
		assertSame(address.getPhone1(), params.get(ReportField.CUSTOMER_ADDRESS_PHONE1.getParamKey()));
		assertSame(address.getPhone2(), params.get(ReportField.CUSTOMER_ADDRESS_PHONE2.getParamKey()));
		assertSame(address.getFax1(), params.get(ReportField.CUSTOMER_ADDRESS_FAX.getParamKey()));
	}
}
