package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class AutoAttributeImportFailureNotification extends ImportFailureNotification {

	public AutoAttributeImportFailureNotification(UserBean notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "autoAttributeImportFailed";
	}

}
