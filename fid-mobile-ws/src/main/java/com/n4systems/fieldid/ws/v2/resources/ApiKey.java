package com.n4systems.fieldid.ws.v2.resources;

import java.util.function.Function;

public abstract class ApiKey<T> {
	private final T sid;

	public ApiKey(String param, Function<String, T> idConverter) {
		String[] parts = param.split("|");
		sid = idConverter.apply(parts[0]);
	}

	public T getSid() {
		return sid;
	}
}
