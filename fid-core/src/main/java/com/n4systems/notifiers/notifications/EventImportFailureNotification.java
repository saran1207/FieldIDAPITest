package com.n4systems.notifiers.notifications;

import com.n4systems.model.EventType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.services.TenantFinder;

public class EventImportFailureNotification extends ImportFailureNotification {
	
	private EventType eventType;
	
	public EventImportFailureNotification(User notifiyUser,  EventType eventType) {
		super(notifiyUser);
		this.eventType = eventType;
	}

	@Override
	public String notificationName() {
		return "eventImportFailed";
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	@Override
	public String subject() {
		return "Import Failed: Event Import for " + getPrimaryOrg(eventType.getTenant().getId()).getName();
	}

	private PrimaryOrg getPrimaryOrg(long tenantId) {
		return TenantFinder.getInstance().findPrimaryOrg(tenantId);
	}
}
