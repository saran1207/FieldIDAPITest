package com.n4systems.fieldid.collection.helpers;

import static com.n4systems.fieldid.collection.helpers.CommonAssetValues.*;

import java.util.List;

import com.n4systems.model.Product;

public class CommonAssetValuesFinder {

	private final List<Product> assets;

	public CommonAssetValuesFinder(List<Product> assets) {
		this.assets = assets;
		
	}

	public CommonAssetValues findCommonValues() {
		
		if (assets.isEmpty())
			return CommonAssetValues.NO_COMMON_VALUES;
		
		CommonAssetValues commonValues = null;
		for (Product asset : assets) {
			if (commonValues == null)
				commonValues = CommonAssetValues.createFrom(asset);
			else
				commonValues = commonValues.findCommon(asset);
		}
		
		return commonValues != null ? commonValues : NO_COMMON_VALUES;
	}

	
}
		