package com.n4systems.model.eventtype;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;


public class EventTypeCleanerAssignToFilter implements Cleaner<EventType>{

	private final SystemSecurityGuard securityGuard;

	public EventTypeCleanerAssignToFilter(SystemSecurityGuard securityGuard) {
		this.securityGuard = securityGuard;
	}

	public void clean(EventType eventType) {
		if (!securityGuard.isAssignedToEnabled()) {
			eventType.removeAssignedTo();
		}
	}
}
