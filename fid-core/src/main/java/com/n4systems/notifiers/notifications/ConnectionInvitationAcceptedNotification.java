package com.n4systems.notifiers.notifications;

public class ConnectionInvitationAcceptedNotification extends Notification {
	private static final String NAME = "safetyNetworkConnectionInvitationAccepted";
	private static final String SUBJECT = "has accepted your invitation on Field ID";

	private String companyName;

	@Override
	public String notificationName() {
		return NAME;
	}

	@Override
	public String subject() {
		return companyName + SPACE + SUBJECT;
	}

	public void setAcceptingCompanyName(String companyName) {
		this.companyName = companyName;
		
	}
	
	public String getAcceptingCompanyName() {
		return companyName;
	}

	

}
