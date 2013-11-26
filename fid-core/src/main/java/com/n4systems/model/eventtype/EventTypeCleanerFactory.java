package com.n4systems.model.eventtype;

import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.model.Tenant;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;

public class EventTypeCleanerFactory {

	public static Cleaner<ThingEventType> cleanerFor(Tenant tenant) {
		AggragateEventTypeCleaner cleaner = new AggragateEventTypeCleaner();
		cleaner.addCleaner(new EventTypeCleaner(tenant));
		cleaner.addCleaner(new EventTypeCleanerAssignToFilter(new SerializableSecurityGuard(tenant)));
		
		return cleaner;
	}

}
