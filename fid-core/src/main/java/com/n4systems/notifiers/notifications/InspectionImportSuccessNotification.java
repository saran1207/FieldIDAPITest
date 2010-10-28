package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class InspectionImportSuccessNotification extends ImportSuccessNotification {

	public InspectionImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "eventImportSuccess";
	}

}
