package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class InspectionImportFailureNotification extends ImportFailureNotification {

	public InspectionImportFailureNotification(UserBean notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "inspectionImportFailed";
	}

}
