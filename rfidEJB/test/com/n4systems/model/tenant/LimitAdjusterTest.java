package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import org.junit.Test;


public class LimitAdjusterTest {

	
	private static final long UNADJUSTABLE_USER_LIMIT = 2L;


	@Test
	public void should_adjust_limits_by_values() {
		TenantLimit expectedTenantLimit = createTenantLimit(30L, 20L, UNADJUSTABLE_USER_LIMIT);
		
		TenantLimit tenantLimitToAdjust = createTenantLimit(21L, 3L, UNADJUSTABLE_USER_LIMIT);
		LimitAdjuster sut = new LimitAdjuster(17L, 9L);
		
		// exercise
		sut.adjustLimits(tenantLimitToAdjust);
		
		// verify
		assertEquals(expectedTenantLimit, tenantLimitToAdjust);
	}
	
	
	@Test
	public void should_add_limits_with_original_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, UNADJUSTABLE_USER_LIMIT);
		
		TenantLimit tenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, UNADJUSTABLE_USER_LIMIT);
		LimitAdjuster sut = new LimitAdjuster(7L, 9L);
		
		// exercise
		sut.adjustLimits(tenantLimit);
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	
	@Test
	public void should_adjust_limits_with_limit_being_added_on_has_all_ajustable_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, UNADJUSTABLE_USER_LIMIT);
		
		TenantLimit tenantLimit = createTenantLimit(1L, 3L, UNADJUSTABLE_USER_LIMIT);
		LimitAdjuster sut = new LimitAdjuster(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.adjustLimits(tenantLimit);
		
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	@Test
	public void should_adjust_limits_with_limits_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, UNADJUSTABLE_USER_LIMIT);
		
		TenantLimit tenantLimit = createTenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, UNADJUSTABLE_USER_LIMIT);
		LimitAdjuster sut = new LimitAdjuster(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.adjustLimits(tenantLimit);
		
		// verify
		assertEquals(expectedTenantLimit, tenantLimit);
	}
	
	
	private TenantLimit createTenantLimit(long diskSpace, long assets, long users) {
		TenantLimit tenantLimit = new TenantLimit();
		
		tenantLimit.setAssets(assets);
		tenantLimit.setUsers(users);
		tenantLimit.setDiskSpaceInBytes(diskSpace);
		
		return tenantLimit;
	}
}
