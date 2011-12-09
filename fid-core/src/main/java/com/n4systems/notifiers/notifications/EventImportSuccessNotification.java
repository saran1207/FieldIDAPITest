package com.n4systems.notifiers.notifications;

import com.n4systems.model.EventType;
import com.n4systems.model.user.User;

public class EventImportSuccessNotification extends ImportSuccessNotification {
	
	private EventType eventType;

	public EventImportSuccessNotification(User notifyUser, EventType eventType) {
		super(notifyUser);
		this.eventType = eventType;
	}

	@Override
	public String notificationName() {
		return "eventImportSuccess";
	}

	public EventType getEventType() {
		return eventType;
	}
	
	@Override
	public String subject() {
		return "Import Completed: Event Import for " + getPrimaryOrg(eventType.getTenant().getId()).getName();
	}
}
