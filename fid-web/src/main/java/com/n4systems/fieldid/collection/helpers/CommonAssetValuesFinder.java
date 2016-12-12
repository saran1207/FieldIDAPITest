package com.n4systems.fieldid.collection.helpers;

import com.n4systems.model.Asset;

import java.util.List;

import static com.n4systems.fieldid.collection.helpers.CommonAssetValues.NO_COMMON_VALUES;

public class CommonAssetValuesFinder {

	private final List<Asset> assets;

	public CommonAssetValuesFinder(List<Asset> assets) {
		this.assets = assets;
		
	}

	public CommonAssetValues findCommonValues() {
		
		if (assets.isEmpty())
			return CommonAssetValues.NO_COMMON_VALUES;
		
		CommonAssetValues commonValues = null;
		for (Asset asset : assets) {
			if (commonValues == null)
				commonValues = CommonAssetValues.createFrom(asset);
			else
				commonValues = commonValues.findCommon(asset);
		}
		
		return commonValues != null ? commonValues : NO_COMMON_VALUES;
	}

	
}
		