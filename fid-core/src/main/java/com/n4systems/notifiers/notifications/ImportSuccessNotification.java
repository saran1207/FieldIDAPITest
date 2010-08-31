package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public abstract class ImportSuccessNotification extends Notification {
	private int totalImported = 0;

	public ImportSuccessNotification(User notifyUser) {
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
