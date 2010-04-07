package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.AddressInfoBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;
import com.n4systems.util.ConfigContextRequiredTestCase;

public class OrganizationMapBuilderTest extends ConfigContextRequiredTestCase{

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		Transaction transaction = new DummyTransaction();
		
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		InternalOrg org = OrgBuilder.aPrimaryOrg().buildPrimary();
		org.setCertificateName(TestHelper.randomString());
		org.setAddressInfo(AddressInfoBuilder.anAddress().build());
		
		MapBuilder<AddressInfo> addressMapBuilder = EasyMock.createMock(MapBuilder.class);
		addressMapBuilder.addParams(reportMap, org.getAddressInfo(), transaction);
		EasyMock.replay(addressMapBuilder);
		
		OrganizationMapBuilder builder = new OrganizationMapBuilder(addressMapBuilder);
		builder.addParams(reportMap, org, transaction);
		
		assertEquals(org.getCertificateName(), reportMap.get(ReportField.CERTIFICATE_NAME.getParamKey()));
	}
	
}
