package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public abstract class ImportFailureNotification extends Notification {

	public ImportFailureNotification(User notifiyUser) {
		notifiyUser(notifiyUser);
	}
	
	@Override
	public String subject() {
		return "Import Failed";
	}
	
	public String getHelpUrl() {
		return ConfigContext.getCurrentContext().getString(ConfigEntry.HELP_SYSTEM_URL);
	}
	
}
