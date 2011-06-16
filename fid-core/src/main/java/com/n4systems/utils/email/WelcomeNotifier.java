package com.n4systems.utils.email;

import com.n4systems.model.user.User;

public interface WelcomeNotifier {

	void sendPersonalizedWelcomeNotificationTo(User user, String personalMessage);

	void sendWelcomeNotificationTo(User user);

}
