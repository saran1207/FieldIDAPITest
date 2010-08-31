package com.n4systems.fieldid.actions.product.helpers;

import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.HasLinkedProductsLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

public class ProductLinkedHelper {
	public static boolean isLinked(Product product, LoaderFactory loaderFactory) {
		if (product == null) {
			return false;
		} else if (product.isLinked()) {
			return true;
		}
		
		// this checks if there are any products linked to this product
		HasLinkedProductsLoader hasLinkedLoader = loaderFactory.createHasLinkedProductsLoader();
		hasLinkedLoader.setNetworkId(product.getNetworkId());
		hasLinkedLoader.setProductId(product.getId());
		
		boolean hasLinked = hasLinkedLoader.load();
		return hasLinked;		
	}
}
