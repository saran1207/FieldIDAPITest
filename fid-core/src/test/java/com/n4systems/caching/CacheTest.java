package com.n4systems.caching;

import com.n4systems.caching.safetynetwork.VendorListCacheStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CacheTest {

	@Before
	@After
	public void clear_cache_cache() {
		Cache.cacheMap.clear();
	}
	
	@Test
	public void cache_creates_and_caches_cache_stores() {
		assertTrue(Cache.cacheMap.isEmpty());
		
		Cache.getVendorListStore();
		
		assertFalse(Cache.cacheMap.isEmpty());
	}
	
	@Test
	public void get_vendor_list_store_returns_correct_store() {
		assertTrue(Cache.getVendorListStore() instanceof VendorListCacheStore);
	}
}
