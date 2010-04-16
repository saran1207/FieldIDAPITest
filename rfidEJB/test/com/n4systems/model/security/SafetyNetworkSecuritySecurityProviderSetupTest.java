package com.n4systems.model.security;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;


public class SafetyNetworkSecuritySecurityProviderSetupTest {
	SafetyNetworkSecurityLevelProvider sut = new ThreadSafeSafetyNetworkSecurityLevelProvider();
	
	
	
	@Test
	public void should_be_able_to_add_an_org_connection_to_the_cache() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		
		assertThat(sut.isEmpty(), equalTo(false));
	}
	
	@Test
	public void should_respond_many_away_for_orgs_it_can_not_find_connections_for() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		
		assertThat(sut.getConnectionSecurityLevel(vendor, customer), equalTo(SecurityLevel.MANY_AWAY));
	}
	
	@Test
	public void should_always_respond_with_local_connection_if_the_2_orgs_are_the_same_org_with_no_connection_registered() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		
		assertThat(sut.getConnectionSecurityLevel(vendor, vendor), equalTo(SecurityLevel.LOCAL));
	}
	
	@Test
	public void should_always_respond_with_local_connection_if_the_2_orgs_are_the_same_org_with_connection_registered() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		
		assertThat(sut.getConnectionSecurityLevel(vendor, vendor), equalTo(SecurityLevel.LOCAL));
	}
	
	@Test
	public void should_clear_the_cache_the_cache_of_all_connections() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		sut.clear();
		
		assertThat(sut.isEmpty(), equalTo(true));
	}
	
	@Test
	public void should_not_find_a_direct_connection_between_the_orgs_connected_after_a_clear() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		sut.clear();
		
		assertThat(sut.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer()), equalTo(SecurityLevel.MANY_AWAY));
	}
	
	
	@Test
	public void should_find_a_direct_connection_between_the_orgs_connected() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		
		assertThat(sut.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer()), equalTo(SecurityLevel.DIRECT));
	}
	
	
	@Test
	public void should_allow_the_same_connection_to_be_given_twice() throws Exception {
		PrimaryOrg vendor = aPrimaryOrg().build();
		PrimaryOrg customer = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(vendor, customer); 
		
		sut.connect(connection);
		sut.connect(connection);
		
		assertThat(sut.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer()), equalTo(SecurityLevel.DIRECT));
	}
	
	@Test
	public void should_find_direct_connections_between_the_org_2_sets_of_orgs() throws Exception {
		OrgConnection connection = new OrgConnection(aPrimaryOrg().build(), aPrimaryOrg().build());
		OrgConnection connection2 = new OrgConnection(aPrimaryOrg().build(), aPrimaryOrg().build());
		
		sut.connect(connection);
		sut.connect(connection2);
		
		assertThat(sut.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer()), equalTo(SecurityLevel.DIRECT));
		assertThat(sut.getConnectionSecurityLevel(connection2.getVendor(), connection2.getCustomer()), equalTo(SecurityLevel.DIRECT));
	}
	
	@Test
	public void should_find_direct_connection_when_2_connections_with_one_shared_org_are_added() throws Exception {
		PrimaryOrg sharedOrg = aPrimaryOrg().build();
		OrgConnection connection = new OrgConnection(sharedOrg, aPrimaryOrg().build()); 
		OrgConnection connection2 = new OrgConnection(sharedOrg, aPrimaryOrg().build());
		
		sut.connect(connection);
		sut.connect(connection2);
		
		assertThat(sut.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer()), equalTo(SecurityLevel.DIRECT));
		assertThat(sut.getConnectionSecurityLevel(connection2.getVendor(), connection2.getCustomer()), equalTo(SecurityLevel.DIRECT));
	}
}
