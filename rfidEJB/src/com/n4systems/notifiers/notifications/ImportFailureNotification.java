package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public abstract class ImportFailureNotification extends Notification {

	public ImportFailureNotification(UserBean notifiyUser) {
		notifiyUser(notifiyUser);
	}
	
	@Override
	public String subject() {
		return "Import Failed";
	}
	
}
