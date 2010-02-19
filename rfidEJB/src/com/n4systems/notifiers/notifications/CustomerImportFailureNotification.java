package com.n4systems.notifiers.notifications;

public class CustomerImportFailureNotification extends Notification {

	@Override
	public String notificationName() {
		return "customerImportFailed";
	}

	@Override
	public String subject() {
		return "Customer Import Failed";
	}

}
