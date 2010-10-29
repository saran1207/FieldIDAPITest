package com.n4systems.fieldid.actions.asset.helpers;

import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.HasLinkedAssetsLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

public class AssetLinkedHelper {
	public static boolean isLinked(Asset asset, LoaderFactory loaderFactory) {
		if (asset == null) {
			return false;
		} else if (asset.isLinked()) {
			return true;
		}
		
		// this checks if there are any products linked to this asset
		HasLinkedAssetsLoader hasLinkedLoader = loaderFactory.createHasLinkedAssetsLoader();
		hasLinkedLoader.setNetworkId(asset.getNetworkId());
		hasLinkedLoader.setAssetId(asset.getId());
		
		boolean hasLinked = hasLinkedLoader.load();
		return hasLinked;		
	}
}
