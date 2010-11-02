package com.n4systems.model.inspectiontype;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;

public class EventTypeCleanerFactory {

	public static Cleaner<EventType> cleanerFor(Tenant tenant) {
		AggragateEventTypeCleaner cleaner = new AggragateEventTypeCleaner();
		cleaner.addCleaner(new EventTypeCleaner(tenant));
		cleaner.addCleaner(new EventTypeCleanerAssignToFilter(new SerializableSecurityGuard(tenant)));
		
		return cleaner;
	}

}
