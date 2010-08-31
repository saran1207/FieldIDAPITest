package com.n4systems.model.security;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.safetynetwork.OrgConnection;


public class SafetyNetworkSecurityCacheSetupTest {
	
	@Before
	public void initializeStaticCache() {
		SafetyNetworkSecurityCache.initialize();
	}
	
	@After
	public void cleanOutTheStaticCache() {
		SafetyNetworkSecurityCache.initialize();
	}
	
	
	@Test
	public void should_create_a_new_instance_of_a_secuirty_level_provider_when_asked_to_initialize() throws Exception {
		SafetyNetworkSecurityLevelProvider oldInstance = SafetyNetworkSecurityCache.getInstance();
		SafetyNetworkSecurityCache.initialize();
		SafetyNetworkSecurityLevelProvider newInstance = SafetyNetworkSecurityCache.getInstance();
		
		assertThat(newInstance, is(not(sameInstance(oldInstance))));
	}
	
	
	@Test
	public void should_get_the_same_instance_with_multiple_calls_to_get_instance() throws Exception {
		SafetyNetworkSecurityLevelProvider oldInstance = SafetyNetworkSecurityCache.getInstance();
		SafetyNetworkSecurityLevelProvider newInstance = SafetyNetworkSecurityCache.getInstance();
		
		assertThat(newInstance, is(sameInstance(oldInstance)));
	}
	
	
	@Test
	public void should_initialize_to_an_empty_security_level_provider() throws Exception {
		SafetyNetworkSecurityLevelProvider provider = SafetyNetworkSecurityCache.getInstance();
		
		assertThat(provider.connectionCount(), equalTo(0));
	}
	
	
	@Test
	public void should_add_connection_to_the_singleton_instance() throws Exception {
		
		SafetyNetworkSecurityLevelProvider provider = SafetyNetworkSecurityCache.getInstance();
		SafetyNetworkSecurityCache.recordConnection(new OrgConnection(aPrimaryOrg().build(), aPrimaryOrg().build()));
		
		assertThat(provider.connectionCount(), equalTo(1));
	}
	
	@Test
	public void should_find_the_same_result_for_the_org_security_level_as_the_singlton_instance() throws Exception {
		
		SafetyNetworkSecurityLevelProvider provider = SafetyNetworkSecurityCache.getInstance();
		OrgConnection connection = new OrgConnection(aPrimaryOrg().build(), aPrimaryOrg().build());
		SafetyNetworkSecurityCache.recordConnection(connection);
		
		SecurityLevel staticCacheRepsonse = SafetyNetworkSecurityCache.getSecurityLevel(connection.getVendor(), connection.getCustomer());
		SecurityLevel singletonInstanceResponse = provider.getConnectionSecurityLevel(connection.getVendor(), connection.getCustomer());
		
		assertThat(staticCacheRepsonse, equalTo(singletonInstanceResponse));
	}
	
	
	
	
}
