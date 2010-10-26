package com.n4systems.notifiers.notifications;

import com.n4systems.model.user.User;

public class AssetImportSuccessNotification extends ImportSuccessNotification {

	public AssetImportSuccessNotification(User notifyUser) {
		super(notifyUser);
	}

	@Override
	public String notificationName() {
		return "assetImportSuccess";
	}

}
