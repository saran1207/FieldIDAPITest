package com.n4systems.model.inspectiontype;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;

public class InspectionTypeCleanerFactory {

	public static Cleaner<InspectionType> cleanerFor(Tenant tenant) {
		AggragateInspectionTypeCleaner cleaner = new AggragateInspectionTypeCleaner();
		cleaner.addCleaner(new InspectionTypeCleaner(tenant));
		cleaner.addCleaner(new InspectionTypeCleanerAssignToFilter(new SerializableSecurityGuard(tenant)));
		
		return cleaner;
	}

}
