package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class UserImportSuccessNotification extends ImportSuccessNotification {

	public UserImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "userImportSuccess";
	}
	
	@Override
	public String subject() {
		return "Import Completed: User Import for " + getPrimaryOrg(getTo().getTenant().getId()).getName();
	}

}
