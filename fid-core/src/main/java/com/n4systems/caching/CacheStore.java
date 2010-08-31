package com.n4systems.caching;

public interface CacheStore<K extends CacheKey, V> {
	public V get(K key);
	public void load(K key);
	public void put(K key, V value);
	public void expire(K key);
	public void expireAll();
	public boolean contains(K key);
	public int size();
}
