package com.n4systems.fieldid.actions.user;


import com.n4systems.model.user.User;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.UserWelcomeEmail;
import com.n4systems.util.uri.ActionURLBuilder;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserWelcomeNotificationProducer implements WelcomeNotifier {

	private final Notifier notifier;
	private final String loginUrl;
	private final String forgotPasswordUrl;

	
	public UserWelcomeNotificationProducer(Notifier notifier, ActionURLBuilder urlBuilder) {
		this.notifier = notifier;
		this.loginUrl = urlBuilder.setAction("login").build();
		this.forgotPasswordUrl = urlBuilder.setAction("forgotPassword").build();
	}

	
	@Override
	public void sendWelcomeNotificationTo(User user) {
		sendNotification(user, null);
	}


	private void sendNotification(User user, String personalMessage) {
		UserWelcomeEmail notification = new UserWelcomeEmail(user, loginUrl, forgotPasswordUrl);
		notification.setPersonalMessage(personalMessage);
		notifier.notify(notification );
	}
	
	


	@Override
	public void sendPersonalizedWelcomeNotificationTo(User user, String personalMessage) {
		sendNotification(user, personalMessage);
	}

}
