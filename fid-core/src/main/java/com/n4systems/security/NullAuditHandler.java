package com.n4systems.security;

import com.n4systems.model.Inspection;

public class NullAuditHandler implements AuditHandler {

	public String getMessage(Inspection inspection) {
		return "";
	}

}
