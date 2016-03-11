package com.n4systems.caching;

import com.n4systems.caching.safetynetwork.VendorListCacheStore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CacheTypeTest {

	@Test
	public void vendorlist_creates_correct_store() {
		assertTrue(CacheType.VENDORLIST.createCache() instanceof VendorListCacheStore);
	}
}
