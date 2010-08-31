package com.n4systems.security;

import com.n4systems.model.Inspection;

public interface AuditLogger {

	public void audit(String methodName, Inspection inspection, Throwable t);

}