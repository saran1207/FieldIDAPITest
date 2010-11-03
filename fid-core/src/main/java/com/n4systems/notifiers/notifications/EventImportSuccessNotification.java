package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class EventImportSuccessNotification extends ImportSuccessNotification {

	public EventImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "eventImportSuccess";
	}

}
