package com.n4systems.security;

import com.n4systems.model.Inspection;
import com.n4systems.util.StringUtils;

public class CreateInspectionAuditHandler implements AuditHandler {


	public String getMessage(Inspection inspection) {
		return "\nCreate Inspection:\n" + StringUtils.indent(inspection.toString(), 1) + "\n";
	}

}
