package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class EventImportFailureNotification extends ImportFailureNotification {

	public EventImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "eventImportFailed";
	}

}
