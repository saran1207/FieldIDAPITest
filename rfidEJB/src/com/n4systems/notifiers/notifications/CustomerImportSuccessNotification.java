package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class CustomerImportSuccessNotification extends ImportSuccessNotification {
	
	public CustomerImportSuccessNotification(UserBean notifyUser) {
		super(notifyUser);
	}
	
	@Override
	public String notificationName() {
		return "customerImportSuccess";
	}

}
