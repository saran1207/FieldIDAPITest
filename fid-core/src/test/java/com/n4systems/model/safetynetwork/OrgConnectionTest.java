package com.n4systems.model.safetynetwork;

import com.n4systems.model.orgs.PrimaryOrg;
import org.junit.Test;

import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class OrgConnectionTest {
	
	private PrimaryOrg vendor = aPrimaryOrg().build();
	private PrimaryOrg customer = aPrimaryOrg().build();

	@Test(expected=IllegalArgumentException.class)
	public void should_not_allow_a_connection_between_the_same_primary_orgs() throws Exception {
		new OrgConnection(vendor, vendor);
	}
	
	
	
	@Test
	public void should_allow_a_connection_between_two_different_primary_orgs() throws Exception {
		OrgConnection sut = new OrgConnection(vendor, customer);
		
		assertThat(sut.getCustomer(), equalTo(customer));
		assertThat(sut.getVendor(), equalTo(vendor));
	}
	
	
	
	@Test
	public void should_find_the_customer_org_for_a_customer_connection_type() throws Exception {
		OrgConnection sut = new OrgConnection(vendor, customer);
		
		
		PrimaryOrg actualConnectionOrg = sut.getByConnectionType(OrgConnectionType.CUSTOMER);
		
		assertThat(actualConnectionOrg, equalTo(customer));
		
	}
	
	@Test
	public void should_find_the_vendor_org_for_a_vendor_connection_type() throws Exception {
		OrgConnection sut = new OrgConnection(vendor, customer);
		
		PrimaryOrg actualConnectionOrg = sut.getByConnectionType(OrgConnectionType.VENDOR);
		
		assertThat(actualConnectionOrg, equalTo(vendor));
		
	}

}
