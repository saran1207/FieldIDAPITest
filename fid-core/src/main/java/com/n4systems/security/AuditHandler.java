package com.n4systems.security;

import com.n4systems.model.Inspection;

public interface AuditHandler {
	public String getMessage(Inspection inspection);
}
