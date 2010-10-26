package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class AssetImportFailureNotification extends ImportFailureNotification {

	public AssetImportFailureNotification(User notifiyUser) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "assetImportFailed";
	}

}
