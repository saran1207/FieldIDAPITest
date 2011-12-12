package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class CustomerImportFailureNotification extends ImportFailureNotification {

	private String label;
	
	public CustomerImportFailureNotification(User notifiyUser, String label) {
		super(notifiyUser);
		this.label = label;
	}

	@Override
	public String notificationName() {
		return "customerImportFailed";
	}
	
	@Override
	public String subject() {
		return "Import Failed: " + label + " Import for " + getPrimaryOrg(getTo().getTenant().getId()).getName();
	}

	public String getLabel() {
		return label;
	}


}
