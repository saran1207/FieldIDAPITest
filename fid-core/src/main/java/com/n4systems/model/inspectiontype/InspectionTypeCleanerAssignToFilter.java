package com.n4systems.model.inspectiontype;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Cleaner;


public class InspectionTypeCleanerAssignToFilter implements Cleaner<InspectionType>{

	private final SystemSecurityGuard securityGuard;

	public InspectionTypeCleanerAssignToFilter(SystemSecurityGuard securityGuard) {
		this.securityGuard = securityGuard;
	}

	public void clean(InspectionType inspectionType) {
		if (!securityGuard.isAssignedToEnabled()) {
			inspectionType.removeAssignedTo();
		}
	}
}
