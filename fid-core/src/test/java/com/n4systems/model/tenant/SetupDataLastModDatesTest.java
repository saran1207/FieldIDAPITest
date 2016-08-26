package com.n4systems.model.tenant;


import com.n4systems.model.Tenant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SetupDataLastModDatesTest {
	@Test
	public void should_set_tenant_and_tenant_id_since_hibernate_will_not_propogate_that_information_correctly() {
		Tenant tenant = new Tenant();
		
		tenant.setId(12345L);
		
		SetupDataLastModDates setupModDates = new SetupDataLastModDates();
		
		setupModDates.setTenant(tenant);
		
		assertSame(tenant, setupModDates.getTenant());
		assertEquals(tenant.getId(), setupModDates.getTenantId());
	}
}
