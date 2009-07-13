package com.n4systems.security;

import com.n4systems.model.Inspection;
import com.n4systems.util.StringUtils;

import javax.interceptor.InvocationContext;

public class CreateInspectionAuditHandler implements AuditHandler {

	public String getMessage(InvocationContext ctx) {
		Inspection inspection = (Inspection)ctx.getParameters()[0];
		return "\nCreate Inspection:\n" + StringUtils.indent(inspection.toString(), 1) + "\n";
	}

}
