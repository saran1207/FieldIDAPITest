package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class ProductImportFailureNotification extends ImportFailureNotification {

	public ProductImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "productImportFailed";
	}

}
