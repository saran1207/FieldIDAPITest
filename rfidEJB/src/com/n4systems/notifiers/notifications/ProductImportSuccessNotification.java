package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class ProductImportSuccessNotification extends ImportSuccessNotification {

	public ProductImportSuccessNotification(UserBean notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "productImportSuccess";
	}

}
