package com.n4systems.util.reflection;

public interface ReflectionFilter<T> {
	public String getPath();
	public boolean isValid(T object);
}
