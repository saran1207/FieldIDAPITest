package com.n4systems.handlers.creator.safetynetwork;

import com.n4systems.notifiers.notifications.Notification;

public class ConnectionInviteReceivedNotification extends Notification {

	private static final String NAME = "safetyNetworkInvitaionNotification";
	private String subject;
	private String companyName;
	private String message;
	private String messageUrl;

	@Override
	public String notificationName() {
		return NAME;
	}

	@Override
	public String subject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}
}
