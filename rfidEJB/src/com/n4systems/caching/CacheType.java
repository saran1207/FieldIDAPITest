package com.n4systems.caching;

import com.n4systems.caching.safetynetwork.VendorListCacheStore;

public enum CacheType {
	VENDORLIST;
	
	public CacheStore<? extends CacheKey, ?> createCache() {
		switch (this) {
			case VENDORLIST:
				return new VendorListCacheStore();
		}
		
		// we should have returned by now
		throw new IllegalArgumentException("Unhandled CacheType " + name());
	}
}
