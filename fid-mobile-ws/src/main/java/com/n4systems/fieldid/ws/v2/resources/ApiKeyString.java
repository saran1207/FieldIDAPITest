package com.n4systems.fieldid.ws.v2.resources;

public class ApiKeyString implements ApiKey<String> {
	private final String sid;

	public ApiKeyString(String param) {
		String[] parts = param.split("\\|");
		sid = parts[0];
	}

	public String getSid() {
		return sid;
	}
}
