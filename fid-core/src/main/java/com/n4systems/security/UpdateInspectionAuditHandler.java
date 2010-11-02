package com.n4systems.security;

import com.n4systems.model.Event;
import com.n4systems.util.StringUtils;

public class UpdateInspectionAuditHandler implements AuditHandler {
	

	public String getMessage(Event event) {
		return "\nUpdate Inspection:\n" + StringUtils.indent(event.toString(), 1) + "\n";
	}
	
}
