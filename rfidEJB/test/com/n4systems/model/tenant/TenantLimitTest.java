package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import org.junit.Test;




public class TenantLimitTest {

	@Test
	public void should_add_limits_together() {
		TenantLimit expectedTenantLimit = new TenantLimit(10L, 20L, 30L, 40L);
		
		TenantLimit sut = new TenantLimit(1L, 2L, 3L, 4L);
		
		// exercise
		sut.addDiskSpace(9L);
		sut.addAssets(18L);
		sut.addUsers(27L);
		sut.addSecondaryOrgs(36L);
		
		// verify
		assertEquals(expectedTenantLimit, sut);

	}
	
	@Test
	public void should_add_limits_with_original_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.addDiskSpace(9L);
		sut.addAssets(18L);
		sut.addUsers(27L);
		sut.addSecondaryOrgs(36L);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	
	@Test
	public void should_add_limits_with_limit_being_added_on_has_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(1L, 3L, 2L, 4L);
		
		// exercise
		sut.addDiskSpace(TenantLimit.UNLIMITED);
		sut.addAssets(TenantLimit.UNLIMITED);
		sut.addUsers(TenantLimit.UNLIMITED);
		sut.addSecondaryOrgs(TenantLimit.UNLIMITED);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	@Test
	public void should_add_limits_with_both_limits_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.addDiskSpace(TenantLimit.UNLIMITED);
		sut.addAssets(TenantLimit.UNLIMITED);
		sut.addUsers(TenantLimit.UNLIMITED);
		sut.addSecondaryOrgs(TenantLimit.UNLIMITED);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	

	
}
