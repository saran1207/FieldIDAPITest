package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class CustomerImportSuccessNotification extends ImportSuccessNotification {
	
	public CustomerImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}
	
	@Override
	public String notificationName() {
		return "customerImportSuccess";
	}

}
