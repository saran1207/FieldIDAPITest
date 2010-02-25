package com.n4systems.api.conversion.orgs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.DivisionOrg;

public class DivisionOrgToViewConverterTest {
	
	@Test
	public void test_to_view() throws ConversionException {
		DivisionOrgToViewConverter converter = new DivisionOrgToViewConverter();
		
		DivisionOrg model = OrgBuilder.aDivisionOrg().withAllTestData().buildDivision();
		
		FullExternalOrgView view = converter.toView(model);
		
		assertNotNull(view);
		assertTrue(view.isDivision());
		assertNull(view.getParentOrg());
		assertEquals(model.getGlobalId(), view.getGlobalId());
		assertEquals(model.getName(), view.getName());
		assertEquals(model.getCode(), view.getCode());
		assertEquals(model.getContact().getName(), view.getContactName());
		assertEquals(model.getContact().getEmail(), view.getContactEmail());
		assertEquals(model.getAddressInfo().getStreetAddress(), view.getStreetAddress());
		assertEquals(model.getAddressInfo().getCity(), view.getCity());
		assertEquals(model.getAddressInfo().getState(), view.getState());
		assertEquals(model.getAddressInfo().getCountry(), view.getCountry());
		assertEquals(model.getAddressInfo().getZip(), view.getZip());
		assertEquals(model.getAddressInfo().getPhone1(), view.getPhone1());
		assertEquals(model.getAddressInfo().getPhone2(), view.getPhone2());
		assertEquals(model.getAddressInfo().getFax1(), view.getFax1());
	}
}
