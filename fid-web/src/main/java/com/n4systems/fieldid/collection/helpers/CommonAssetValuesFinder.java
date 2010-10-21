package com.n4systems.fieldid.collection.helpers;

import static com.n4systems.fieldid.collection.helpers.CommonAssetValues.*;

import java.util.List;

import com.n4systems.model.Asset;

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
		