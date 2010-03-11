package com.n4systems.fieldid.actions.user;

import rfid.ejb.entity.UserBean;

import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;

public class UserWelcomeNotificationProducer {

	private final Notifier notifier;

	
	public UserWelcomeNotificationProducer(Notifier notifier) {
		this.notifier = notifier;
	}

	
	public void sendWelcomeNotificationTo(UserBean user) {
		sendNotification(user, null);
	}


	private void sendNotification(UserBean user, String personalMessage) {
		UserWelcomeEmail notification = new UserWelcomeEmail(user);
		notification.setPersonalMessage(personalMessage);
		notifier.notify(notification );
	}
	
	


	public void sendPersonalizedWelcomeNotificationTo(UserBean user, String personalMessage) {
		sendNotification(user, personalMessage);
	}

}
