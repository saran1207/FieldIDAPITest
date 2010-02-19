package com.n4systems.notifiers.notifications;

public class CustomerImportSuccessNotification extends Notification {
	private final int totalImported;
	
	public CustomerImportSuccessNotification(int totalImported) {
		this.totalImported = totalImported;
	}
	
	@Override
	public String notificationName() {
		return "customerImportSuccess";
	}

	@Override
	public String subject() {
		return "Customer Import Successful";
	}

	public int getTotalImported() {
		return totalImported;
	}
	
}
