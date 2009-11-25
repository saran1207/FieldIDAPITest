package com.n4systems.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleCacheStore<K extends CacheKey, V> implements CacheStore<K, V> {
	private static final int DEFAULT_INITIAL_CAP = 256;
	private static final float LOAD_FACTOR = 0.75f;
	private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
	
	private final CacheLoader<K, V> loader;
	private final Map<K, V> cache;
	
	protected SimpleCacheStore(CacheLoader<K, V> loader) {
		this.loader = loader;
		
		cache = new ConcurrentHashMap<K, V>(DEFAULT_INITIAL_CAP, LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
	}
	
	public V get(K key) {
		validateKey(key);
		
		if (!cache.containsKey(key)) {
			load(key);
		}
		
		V value = cache.get(key);
		return value;
	}

	public void load(K key) {
		validateKey(key);
		
		V value = loader.load(key);
		put(key, value);
	}
	
	public void put(K key, V value) {
		validateKey(key);
		
		cache.put(key, value);
	}

	private void validateKey(K key) {
		if (key == null) {
			throw new NullPointerException("null keys not allowed");
		}
	}
	
	public void expire(K key) {
		cache.remove(key);
	}
	
	public void expireAll() {
		cache.clear();
	}
	
	public boolean contains(K key) {
		return cache.containsKey(key);
	}
	
	public int size() {
		return cache.size();
	}
	
}
