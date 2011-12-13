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

	@Override
	public String subject() {
		return "Import Completed: Auto Attribute for " + getPrimaryOrg(getTo().getTenant().getId()).getName();
	}

}
