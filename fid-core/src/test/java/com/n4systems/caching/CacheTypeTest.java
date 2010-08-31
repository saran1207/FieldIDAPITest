package com.n4systems.caching;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.caching.safetynetwork.VendorListCacheStore;

public class CacheTypeTest {

	@Test
	public void vendorlist_creates_correct_store() {
		assertTrue(CacheType.VENDORLIST.createCache() instanceof VendorListCacheStore);
	}
}
