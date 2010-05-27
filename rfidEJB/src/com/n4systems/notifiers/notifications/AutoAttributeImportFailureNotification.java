package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class AutoAttributeImportFailureNotification extends ImportFailureNotification {

	public AutoAttributeImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "autoAttributeImportFailed";
	}

}
