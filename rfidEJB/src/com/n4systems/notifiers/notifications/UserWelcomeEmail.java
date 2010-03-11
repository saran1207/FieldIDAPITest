package com.n4systems.notifiers.notifications;

import rfid.ejb.entity.UserBean;

public class UserWelcomeEmail extends Notification {
	
	
	
	private String personalMessage;

	public UserWelcomeEmail(UserBean notifyUser) {
		super();
		notifiyUser(notifyUser);
	}

	@Override
	public String subject() {
		return getTenantName() + " has created a Field ID Account for you";
	}

	@Override
	public String notificationName() {
		return "userWelcomeEmail";
	}
	
	public String getUserName() {
		return getTo().getUserID();
	}
	
	public String getTenantName() {
		return getTo().getOwner().getPrimaryOrg().getName();
	}

	public boolean isResetPasswordSet() {
		return getTo().getResetPasswordKey() != null;
	}

	public boolean isPersonalized() {
		return personalMessage != null;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}

	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}
}