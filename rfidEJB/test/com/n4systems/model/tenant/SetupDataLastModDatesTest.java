package com.n4systems.model.tenant;


import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.Tenant;

public class SetupDataLastModDatesTest {

	@SuppressWarnings("serial")
	private class _SetupDataLastModDates extends SetupDataLastModDates {
		@Override
		public Long getTenantId() {
			return super.getTenantId();
		}
	}
	
	@Test
	public void test_tenantid_set_by_tenant_setter() {
		Tenant tenant = new Tenant();
		
		tenant.setId(12345L);
		
		_SetupDataLastModDates setupModDates = new _SetupDataLastModDates();
		
		setupModDates.setTenant(tenant);
		
		assertSame(tenant, setupModDates.getTenant());
		assertEquals(tenant.getId(), setupModDates.getTenantId());
	}
	
}
