package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.model.builders.ContactBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public class CustomerMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		Transaction transaction = EasyMock.createMock(Transaction.class);
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		CustomerOrg customer = OrgBuilder.aCustomerOrg().buildCustomer();
		customer.setAddressInfo(AddressInfoBuilder.anAddress().build());
		customer.setContact(ContactBuilder.aContact().build());
		
		MapBuilder<AddressInfo> addressMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<Contact> contactMapBuilder = EasyMock.createMock(MapBuilder.class);
		
		CustomerMapBuilder builder = new CustomerMapBuilder(addressMapBuilder, contactMapBuilder);
		addressMapBuilder.addParams(reportMap, customer.getAddressInfo(), transaction);
		contactMapBuilder.addParams(reportMap, customer.getContact(), transaction);
		
		EasyMock.replay(addressMapBuilder);
		EasyMock.replay(contactMapBuilder);
		
		builder.addParams(reportMap, customer, transaction);
		
		assertEquals(customer.getName(), reportMap.get(ReportField.CUSTOMER_NAME.getParamKey()));
		assertEquals(customer.getCode(), reportMap.get(ReportField.CUSTOMER_CODE.getParamKey()));
	}
	
}
