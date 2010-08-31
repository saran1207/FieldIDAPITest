package com.n4systems.reporting.mapbuilders;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.ReportMap;

public class OwnerMapBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAllFields() {
		ReportMap<Object> params = new ReportMap<Object>();
		
		Transaction transaction = new DummyTransaction();
		
		MapBuilder<PrimaryOrg> primaryOrgBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<CustomerOrg> customerOrgBuilder = EasyMock.createMock(MapBuilder.class);
		MapBuilder<DivisionOrg> divisionOrgBuilder = EasyMock.createMock(MapBuilder.class);

		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		primaryOrgBuilder.addParams(params, org.getPrimaryOrg(), transaction);
		customerOrgBuilder.addParams(params, org.getCustomerOrg(), transaction);
		divisionOrgBuilder.addParams(params, org.getDivisionOrg(), transaction);
		
		EasyMock.replay(primaryOrgBuilder);
		EasyMock.replay(customerOrgBuilder);
		EasyMock.replay(divisionOrgBuilder);
		
		OwnerMapBuilder builder = new OwnerMapBuilder(primaryOrgBuilder, customerOrgBuilder, divisionOrgBuilder);
		builder.addParams(params, org, transaction);
	}
	
}
