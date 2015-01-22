package com.n4systems.fieldid.api.pub.mapping;

@FunctionalInterface
public interface SetterReference<T, V> {
	void set(T type, V value);
}
