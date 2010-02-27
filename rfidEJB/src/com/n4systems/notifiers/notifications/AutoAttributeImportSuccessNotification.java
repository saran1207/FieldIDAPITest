package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class AutoAttributeImportSuccessNotification extends ImportSuccessNotification {
	
	public AutoAttributeImportSuccessNotification(UserBean notifyUser) {
		super(notifyUser);
	}
	
	@Override
	public String notificationName() {
		return "autoAttributeImportSuccess";
	}

}
