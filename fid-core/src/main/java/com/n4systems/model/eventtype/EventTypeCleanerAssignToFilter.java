package com.n4systems.model.eventtype;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;


public class EventTypeCleanerAssignToFilter implements Cleaner<ThingEventType>{

	private final SystemSecurityGuard securityGuard;

	public EventTypeCleanerAssignToFilter(SystemSecurityGuard securityGuard) {
		this.securityGuard = securityGuard;
	}

	public void clean(ThingEventType eventType) {
		if (!securityGuard.isAssignedToEnabled()) {
			eventType.removeAssignedTo();
		}
	}
}
