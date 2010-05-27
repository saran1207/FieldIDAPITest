package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class ProductImportSuccessNotification extends ImportSuccessNotification {

	public ProductImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "productImportSuccess";
	}

}
