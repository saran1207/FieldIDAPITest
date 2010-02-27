package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public abstract class ImportSuccessNotification extends Notification {
	private int totalImported = 0;

	public ImportSuccessNotification(UserBean notifyUser) {
		notifiyUser(notifyUser);
	}
	
	@Override
	public String subject() {
		return "Import Successful";
	}
	
	public void setTotalImported(int totalImported) {
		this.totalImported = totalImported;
	}
	
	public int getTotalImported() {
		return totalImported;
	}
	
}
