package com.n4systems.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.n4systems.caching.safetynetwork.VendorListCacheStore;


public class Cache {
	
	protected static final Map<CacheType, CacheStore<? extends CacheKey, ?>> cacheMap = new ConcurrentHashMap<CacheType, CacheStore<? extends CacheKey, ?>>();
	
	protected static CacheStore<? extends CacheKey, ?> getCache(CacheType type) {
		synchronized (cacheMap) {
			if (!cacheMap.containsKey(type)) {
				cacheMap.put(type, type.createCache());
			}
		}
		return cacheMap.get(type);
	}
	
	public static VendorListCacheStore getVendorListStore() {
		return (VendorListCacheStore)getCache(CacheType.VENDORLIST);
	}
}
