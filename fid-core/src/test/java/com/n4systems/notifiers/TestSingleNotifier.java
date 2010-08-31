/**
 * 
 */
package com.n4systems.notifiers;

import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.Notification;

public class TestSingleNotifier implements Notifier {

	public Notification notification;

	@Override
	public boolean notify(Notification notification) {
		this.notification = notification;
		return true;
	}
	
}