package com.n4systems.security;

import com.n4systems.model.Event;

public interface AuditHandler {
	public String getMessage(Event event);
}
