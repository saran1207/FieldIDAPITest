package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class AutoAttributeImportSuccessNotification extends ImportSuccessNotification {
	
	public AutoAttributeImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}
	
	@Override
	public String notificationName() {
		return "autoAttributeImportSuccess";
	}

}
