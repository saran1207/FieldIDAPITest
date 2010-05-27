package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public abstract class ImportFailureNotification extends Notification {

	public ImportFailureNotification(User notifiyUser) {
		notifiyUser(notifiyUser);
	}
	
	@Override
	public String subject() {
		return "Import Failed";
	}
	
}
