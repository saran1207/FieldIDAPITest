package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class UserImportFailureNotification extends ImportFailureNotification {

	public UserImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "userImportFailed";
	}

}
