package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.model.builders.ContactBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public class DivisionMapBuilderTest {

	@Test
	public void testSetAllFields() {
		Transaction transaction = EasyMock.createMock(Transaction.class);
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		DivisionOrg division = OrgBuilder.aDivisionOrg().buildDivision();
		division.setAddressInfo(AddressInfoBuilder.anAddress().build());
		division.setContact(ContactBuilder.aContact().build());
		
		MapBuilder<AddressInfo> addressMapBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<Contact> contactMapBuilder = EasyMock.createMock(MapBuilder.class);
		
		DivisionMapBuilder builder = new DivisionMapBuilder(addressMapBuilder, contactMapBuilder);
		addressMapBuilder.addParams(reportMap, division.getAddressInfo(), transaction);
		contactMapBuilder.addParams(reportMap, division.getContact(), transaction);
		
		EasyMock.replay(addressMapBuilder);
		EasyMock.replay(contactMapBuilder);
		
		builder.addParams(reportMap, division, transaction);
		
		assertEquals(division.getName(), reportMap.get(ReportField.DIVISION_NAME.getParamKey()));
		assertEquals(division.getCode(), reportMap.get(ReportField.DIVISION_CODE.getParamKey()));
	}
	
}
