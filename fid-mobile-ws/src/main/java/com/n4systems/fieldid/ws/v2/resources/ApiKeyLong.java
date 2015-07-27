package com.n4systems.fieldid.ws.v2.resources;

public class ApiKeyLong implements ApiKey<Long> {
	private final Long sid;

	public ApiKeyLong(String param) {
		String[] parts = param.split("\\|");
		sid = Long.parseLong(parts[0]);
	}

	public Long getSid() {
		return sid;
	}
}
