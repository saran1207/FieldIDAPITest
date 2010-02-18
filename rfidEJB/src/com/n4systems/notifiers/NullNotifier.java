package com.n4systems.notifiers;

import com.n4systems.notifiers.notifications.Notification;

public class NullNotifier implements Notifier {

	@Override
	public boolean success(Notification target) {
		return true;
	}

}
