package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import org.junit.Test;




public class TenantLimitTest {

	@Test
	public void should_add_limits_together() {
		TenantLimit expectedTenantLimit = new TenantLimit(10L, 20L, 30L, 40L, 50L);
		
		TenantLimit sut = new TenantLimit(1L, 2L, 3L, 4L, 5L);
		
		// exercise
		sut.addDiskSpace(9L);
		sut.addAssets(18L);
		sut.addUsers(27L);
		sut.addLiteUsers(45L);
		sut.addSecondaryOrgs(36L);
		
		// verify
		assertEquals(expectedTenantLimit, sut);

	}
	
	@Test
	public void should_add_limits_with_original_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.addDiskSpace(9L);
		sut.addAssets(18L);
		sut.addUsers(27L);
		sut.addLiteUsers(45L);
		sut.addSecondaryOrgs(36L);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	
	@Test
	public void should_add_limits_with_limit_being_added_on_has_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(1L, 3L, 2L, 4L, 5L);
		
		// exercise
		sut.addDiskSpace(TenantLimit.UNLIMITED);
		sut.addAssets(TenantLimit.UNLIMITED);
		sut.addUsers(TenantLimit.UNLIMITED);
		sut.addLiteUsers(TenantLimit.UNLIMITED);
		sut.addSecondaryOrgs(TenantLimit.UNLIMITED);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	@Test
	public void should_add_limits_with_both_limits_having_all_limits_set_to_unlimited() {
		TenantLimit expectedTenantLimit = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		TenantLimit sut = new TenantLimit(TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED, TenantLimit.UNLIMITED);
		
		// exercise
		sut.addDiskSpace(TenantLimit.UNLIMITED);
		sut.addAssets(TenantLimit.UNLIMITED);
		sut.addUsers(TenantLimit.UNLIMITED);
		sut.addLiteUsers(TenantLimit.UNLIMITED);
		sut.addSecondaryOrgs(TenantLimit.UNLIMITED);
		
		// verify
		assertEquals(expectedTenantLimit, sut);
	}
	
	

	
	@Test
	public void should_find_if_asset_limit_is_greater_than_a_supplied_limit() throws Exception {
		TenantLimit limit = new TenantLimit();
		limit.setAssets(10L);
		
		TenantLimit unlimitedAssets = new TenantLimit();
		unlimitedAssets.setAssetsUnlimited();
		
		assertTrue(limit.isAssetLimitGreaterThan(1L));
		assertFalse(limit.isAssetLimitGreaterThan(TenantLimit.UNLIMITED));
		assertFalse(limit.isAssetLimitGreaterThan(25L));
		assertTrue(limit.isAssetLimitGreaterThan(10L));
		assertTrue(limit.isAssetLimitGreaterThan(10L));
		
		assertTrue(unlimitedAssets.isAssetLimitGreaterThan(TenantLimit.UNLIMITED));
	}
	
	
	@Test
	public void should_find_if_disk_limit_is_greater_than_a_supplied_limit() throws Exception {
		TenantLimit limit = new TenantLimit();
		limit.setDiskSpaceInBytes(10L);
		
		TenantLimit unlimitedDiskSpace = new TenantLimit();
		unlimitedDiskSpace.setDiskSpaceUnlimited();
		
		assertTrue(limit.isDiskLimitInBytesGreaterThan(1L));
		assertFalse(limit.isDiskLimitInBytesGreaterThan(TenantLimit.UNLIMITED));
		assertFalse(limit.isDiskLimitInBytesGreaterThan(25L));
		assertTrue(limit.isDiskLimitInBytesGreaterThan(10L));
		assertTrue(limit.isDiskLimitInBytesGreaterThan(10L));
		
		assertTrue(unlimitedDiskSpace.isDiskLimitInBytesGreaterThan(TenantLimit.UNLIMITED));
	}
	
	
	
}
