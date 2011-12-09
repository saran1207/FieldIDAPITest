package com.n4systems.notifiers.notifications;

import com.n4systems.model.AssetType;
import com.n4systems.model.user.User;

public class AssetImportFailureNotification extends ImportFailureNotification {
	
	private AssetType assetType;

	public AssetImportFailureNotification(User notifiyUser, AssetType assetType) {
		super(notifiyUser);
	}

	@Override
	public String notificationName() {
		return "assetImportFailed";
	}
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	@Override
	public String subject() {
		return "Import Failed: Asset Import for " + getPrimaryOrg(assetType.getTenant().getId()).getName();
	}

}
