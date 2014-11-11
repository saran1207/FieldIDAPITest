package com.n4systems.fieldid.api.pub.mapping;

public interface Mapper<F, T> {
	void map(F from, T to);
}
