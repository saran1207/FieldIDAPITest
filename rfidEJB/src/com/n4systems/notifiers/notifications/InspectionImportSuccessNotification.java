package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class InspectionImportSuccessNotification extends ImportSuccessNotification {

	public InspectionImportSuccessNotification(UserBean notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "inspectionImportSuccess";
	}

}
