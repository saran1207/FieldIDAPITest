package com.n4systems.fieldid.ws.v2.resources;

import java.util.function.Function;

public class ApiKeyString extends ApiKey<String> {

	public ApiKeyString(String param) {
		super(param, Function.identity());
	}

}
