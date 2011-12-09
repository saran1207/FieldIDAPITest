package com.n4systems.notifiers.notifications;

import com.n4systems.model.AssetType;
import com.n4systems.model.user.User;

public class AssetImportSuccessNotification extends ImportSuccessNotification {
	
	private AssetType assetType;

	public AssetImportSuccessNotification(User notifyUser, AssetType assetType) {
		super(notifyUser);
		this.assetType = assetType;
	}

	@Override
	public String notificationName() {
		return "assetImportSuccess";
	}

	public AssetType getAssetType() {
		return assetType;
	}
	
	@Override
	public String subject() {
		return "Import Completed: Asset Import for " + getPrimaryOrg(assetType.getTenant().getId()).getName();
	}
	
}
