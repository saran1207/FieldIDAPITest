package com.n4systems.security;

import com.n4systems.model.Inspection;
import com.n4systems.util.StringUtils;

public class UpdateInspectionAuditHandler implements AuditHandler {
	

	public String getMessage(Inspection inspection) {
		return "\nUpdate Inspection:\n" + StringUtils.indent(inspection.toString(), 1) + "\n";
	}
	
}
