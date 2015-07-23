package com.n4systems.fieldid.ws.v2.resources.setupdata.autoattribute;

import java.util.HashMap;
import java.util.Map;

public class ApiAutoAttributeDefinition {
	private Map<Long, Long> in = new HashMap<>();
	private Map<Long, Object> out = new HashMap<>();

	public Map<Long, Long> getIn() {
		return in;
	}

	public void setIn(Map<Long, Long> in) {
		this.in = in;
	}

	public Map<Long, Object> getOut() {
		return out;
	}

	public void setOut(Map<Long, Object> out) {
		this.out = out;
	}
}
