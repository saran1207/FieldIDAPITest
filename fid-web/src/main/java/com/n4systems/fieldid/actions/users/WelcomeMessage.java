package com.n4systems.fieldid.actions.users;

import com.n4systems.util.StringUtils;

public class WelcomeMessage {
	private boolean sendEmail = false;
	private String personalMessage = null;
	
	
	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = StringUtils.isNotEmpty(personalMessage) ? personalMessage : null;
	}
	public String getPersonalMessage() {
		return personalMessage;
	}
	
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	public boolean isSendEmail() {
		return sendEmail;
	}
	public boolean isPersonalMessageProvided() {
		return personalMessage != null;
	}

	
}