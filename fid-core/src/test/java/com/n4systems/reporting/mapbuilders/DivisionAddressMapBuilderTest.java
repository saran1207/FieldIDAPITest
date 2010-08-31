package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.util.ReportMap;

public class DivisionAddressMapBuilderTest {

	@Test
	public void testSetAllFields() {
		AddressInfo address = AddressInfoBuilder.anAddress().build();
		
		DivisionAddressMapBuilder builder = new DivisionAddressMapBuilder();

		ReportMap<Object> params = new ReportMap<Object>();
		builder.addParams(params, address, null);
		
		assertSame(address.getStreetAddress(), params.get(ReportField.DIVISION_ADDRESS_STREET.getParamKey()));
		assertSame(address.getCity(), params.get(ReportField.DIVISION_ADDRESS_CITY.getParamKey()));
		assertSame(address.getState(), params.get(ReportField.DIVISION_ADDRESS_STATE.getParamKey()));
		assertSame(address.getZip(), params.get(ReportField.DIVISION_ADDRESS_POSTAL.getParamKey()));
		assertSame(address.getPhone1(), params.get(ReportField.DIVISION_ADDRESS_PHONE1.getParamKey()));
		assertSame(address.getPhone2(), params.get(ReportField.DIVISION_ADDRESS_PHONE2.getParamKey()));
		assertSame(address.getFax1(), params.get(ReportField.DIVISION_ADDRESS_FAX.getParamKey()));
	}
	
}
