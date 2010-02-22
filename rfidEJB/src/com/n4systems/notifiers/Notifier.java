package com.n4systems.notifiers;

import com.n4systems.notifiers.notifications.Notification;


/**
 * A notifier is an optional step in any process.  
 * Notifiers should not throw exceptions up the stack instead they should catch anything and report the status with 
 * a boolean return value.  This will allow the caller to determine the best course of action.  
 * When success returns true it must
 * 
 * @author aaitken
 *
 */
public interface Notifier {

	public boolean notify(Notification notification);
}
