package com.n4systems.fieldid.api.pub.mapping;

@FunctionalInterface
public interface ValueConverter<T, R> {
	public R convert(T value);
}
