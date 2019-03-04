package com.n4systems.fieldid.api.pub.mapping;

@FunctionalInterface
public interface HasserReference<T> {
	boolean has(T type);
}
