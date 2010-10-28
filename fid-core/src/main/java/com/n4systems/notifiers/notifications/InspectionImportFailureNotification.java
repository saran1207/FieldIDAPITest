package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class InspectionImportFailureNotification extends ImportFailureNotification {

	public InspectionImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "eventImportFailed";
	}

}
