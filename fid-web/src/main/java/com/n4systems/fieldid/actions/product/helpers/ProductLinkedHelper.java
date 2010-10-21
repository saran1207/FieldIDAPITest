package com.n4systems.fieldid.actions.product.helpers;

import com.n4systems.model.Asset;
import com.n4systems.model.safetynetwork.HasLinkedProductsLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

public class ProductLinkedHelper {
	public static boolean isLinked(Asset asset, LoaderFactory loaderFactory) {
		if (asset == null) {
			return false;
		} else if (asset.isLinked()) {
			return true;
		}
		
		// this checks if there are any products linked to this asset
		HasLinkedProductsLoader hasLinkedLoader = loaderFactory.createHasLinkedProductsLoader();
		hasLinkedLoader.setNetworkId(asset.getNetworkId());
		hasLinkedLoader.setProductId(asset.getId());
		
		boolean hasLinked = hasLinkedLoader.load();
		return hasLinked;		
	}
}
