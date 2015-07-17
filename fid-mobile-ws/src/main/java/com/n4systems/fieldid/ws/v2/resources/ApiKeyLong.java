package com.n4systems.fieldid.ws.v2.resources;

public class ApiKeyLong extends ApiKey<Long> {

	public ApiKeyLong(String param) {
		super(param, Long::parseLong);
	}

}
