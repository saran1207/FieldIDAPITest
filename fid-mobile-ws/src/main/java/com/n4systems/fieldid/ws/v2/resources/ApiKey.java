package com.n4systems.fieldid.ws.v2.resources;

import java.util.List;
import java.util.stream.Collectors;

public interface ApiKey<T> {
	T getSid();

	static <T, K extends ApiKey<T>> List<T> unwrap(List<K> keys) {
		return keys.stream().map(ApiKey::getSid).collect(Collectors.toList());
	}
}
