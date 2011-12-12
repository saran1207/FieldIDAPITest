package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class CustomerImportSuccessNotification extends ImportSuccessNotification {
	
	private String label;
	
	public CustomerImportSuccessNotification(User notifyUser, String label) {
		super(notifyUser);
		this.label = label;
	}
	
	@Override
	public String notificationName() {
		return "customerImportSuccess";
	}
	
	@Override
	public String subject() {
		return "Import Completed: " + label + " Import for " + getPrimaryOrg(getTo().getTenant().getId()).getName();
	}

	public String getLabel() {
		return label;
	}

}
