package com.n4systems.security;

import com.n4systems.model.Event;

public class NullAuditHandler implements AuditHandler {

	public String getMessage(Event event) {
		return "";
	}

}
