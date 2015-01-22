package com.n4systems.fieldid.api.pub.mapping;

@FunctionalInterface
public interface GetterReference<T, R> {
	R get(T type);
}
