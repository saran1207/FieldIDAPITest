package com.n4systems.model.tenant;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.model.TenantOrganization;

public class SetupDataLastModDatesTest {

	@SuppressWarnings("serial")
	private class _SetupDataLastModDates extends SetupDataLastModDates {
		@Override
		public Long getR_tenant() {
			return super.getR_tenant();
		}
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test_tenantid_set_by_tenant_setter() {
		TenantOrganization tenant = new TenantOrganization() {
			@Override
			public List<? extends TenantOrganization> getLinkedTenants() {
				return null;
			}
		};
		
		tenant.setId(12345L);
		
		_SetupDataLastModDates setupModDates = new _SetupDataLastModDates();
		
		setupModDates.setTenant(tenant);
		
		assertSame(tenant, setupModDates.getTenant());
		assertEquals(tenant.getId(), setupModDates.getR_tenant());
	}
	
}
