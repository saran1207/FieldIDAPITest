package com.n4systems.fieldid.actions.user;


import com.n4systems.model.user.User;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.UserPasswordWelcomeEmail;
import com.n4systems.util.uri.ActionURLBuilder;

public class UserPasswordWelcomeNotificationProducer {

	private final Notifier notifier;
	private final String loginUrl;
	private ActionURLBuilder urlBuilder;

	
	public UserPasswordWelcomeNotificationProducer(Notifier notifier, ActionURLBuilder urlBuilder) {
		this.notifier = notifier;
		this.loginUrl = urlBuilder.setAction("login").build();
		this.urlBuilder = urlBuilder;
	}

	
	public void sendWelcomeNotificationTo(User user) {
		sendNotification(user, null);
	}


	private void sendNotification(User user, String personalMessage) {
		UserPasswordWelcomeEmail notification = new UserPasswordWelcomeEmail(user, loginUrl, getForgetPasswordUrl(user));
		notification.setPersonalMessage(personalMessage);
		notifier.notify(notification);
	}


	private String getForgetPasswordUrl(User user) {
		return urlBuilder.setAction("firstTimeLogin")
                         .addParameter("u", user.getUserID())
                         .addParameter("k", user.getResetPasswordKey())
                         .build();
	}

	public void sendPersonalizedWelcomeNotificationTo(User user, String personalMessage) {
		sendNotification(user, personalMessage);
	}

}
