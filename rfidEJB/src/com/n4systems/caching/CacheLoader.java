package com.n4systems.caching;


public interface CacheLoader<K extends CacheKey, T> {
	public T load(K key);
}
