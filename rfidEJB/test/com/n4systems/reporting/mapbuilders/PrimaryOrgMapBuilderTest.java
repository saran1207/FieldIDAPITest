package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.ReportMap;

public class PrimaryOrgMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		ReportMap<Object> params = new ReportMap<Object>();

		Transaction transaction = new DummyTransaction();
		
		PrimaryOrg org = OrgBuilder.aPrimaryOrg().buildPrimary();
		org.setAddressInfo(AddressInfoBuilder.anAddress().build());
		
		MapBuilder<AddressInfo> addressMapBuilder = EasyMock.createMock(MapBuilder.class);
		addressMapBuilder.addParams(params, org.getAddressInfo(), transaction);
		EasyMock.replay(addressMapBuilder);
		
		PrimaryOrgMapBuilder builder = new PrimaryOrgMapBuilder(addressMapBuilder);
		builder.addParams(params, org, transaction);
		
		assertEquals(org.getName(), params.get(ReportField.PRIMARY_ORG_NAME.getParamKey()));
	}
	
}
