package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class CustomerImportFailureNotification extends ImportFailureNotification {

	public CustomerImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "customerImportFailed";
	}

}
