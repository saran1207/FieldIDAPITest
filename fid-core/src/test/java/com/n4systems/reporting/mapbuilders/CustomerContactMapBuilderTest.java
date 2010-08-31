package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Contact;
import com.n4systems.model.builders.ContactBuilder;
import com.n4systems.util.ReportMap;

public class CustomerContactMapBuilderTest {

	@Test
	public void testSetAllFields() {
		Contact contact = ContactBuilder.aContact().build();
		
		CustomerContactMapBuilder builder = new CustomerContactMapBuilder();

		ReportMap<Object> params = new ReportMap<Object>();
		builder.addParams(params, contact, null);
		
		assertSame(contact.getName(), params.get(ReportField.CUSTOMER_CONTACT_NAME.getParamKey()));
		assertSame(contact.getEmail(), params.get(ReportField.CUSTOMER_CONTACT_EMAIL.getParamKey()));	
	}
}
