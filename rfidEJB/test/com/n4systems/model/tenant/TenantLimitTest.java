package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import org.junit.Test;



public class TenantLimitTest {

	@Test
	public void should_add_limits_together() {
		TenantLimit expectedTenantLimit = createTenantLimit(10L, 20L, 30L);
		
		TenantLimit tenantLimit = createTenantLimit(1L, 2L, 3L);
		TenantLimit tenantLimitToAddOn = createTenantLimit(9L, 18L, 27L);
		
		// exercise
		tenantLimit.addOn(tenantLimitToAddOn);
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);

	}
	
	@Test
	public void should_add_limits_with_original_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit tenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		TenantLimit tenantLimitToAddOn = createTenantLimit(9L, 8L, 7L);
		
		// exercise
		tenantLimit.addOn(tenantLimitToAddOn);
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	
	@Test
	public void should_add_limits_with_limit_being_added_on_has_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit tenantLimit = createTenantLimit(1L, 2L, 3L);
		TenantLimit tenantLimitToAddOn = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		tenantLimit.addOn(tenantLimitToAddOn);
		
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	@Test
	public void should_add_limits_with_both_limits_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit tenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		TenantLimit tenantLimitToAddOn = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		tenantLimit.addOn(tenantLimitToAddOn);
		
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	

	private TenantLimit createTenantLimit(long diskSpace, long users, long assets) {
		TenantLimit tenantLimit = new TenantLimit();
		
		tenantLimit.setAssets(assets);
		tenantLimit.setUsers(users);
		tenantLimit.setDiskSpaceInBytes(diskSpace);
		
		return tenantLimit;
	}
}
