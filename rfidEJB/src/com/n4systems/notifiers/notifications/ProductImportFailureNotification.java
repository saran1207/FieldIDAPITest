package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class ProductImportFailureNotification extends ImportFailureNotification {

	public ProductImportFailureNotification(UserBean notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "productImportFailed";
	}

}
