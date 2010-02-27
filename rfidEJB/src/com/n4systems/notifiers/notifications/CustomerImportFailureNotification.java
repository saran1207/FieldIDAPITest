package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class CustomerImportFailureNotification extends ImportFailureNotification {

	public CustomerImportFailureNotification(UserBean notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "customerImportFailed";
	}

}
