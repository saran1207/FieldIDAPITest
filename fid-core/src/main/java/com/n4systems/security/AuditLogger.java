package com.n4systems.security;

import com.n4systems.model.Event;

public interface AuditLogger {

	public void audit(String methodName, Event event, Throwable t);

}