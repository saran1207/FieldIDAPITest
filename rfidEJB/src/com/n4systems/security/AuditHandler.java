package com.n4systems.security;

import javax.interceptor.InvocationContext;

public interface AuditHandler {
	public String getMessage(InvocationContext ctx);
}
