package com.n4systems.fieldid.actions.helpers;

import java.util.List;
import java.util.SortedSet;

import com.n4systems.ejb.AssetManager;

public class ProductManagerBackedCommonAssetAttributeFinder implements CommonAssetAttributeFinder {
	private final AssetManager assetManager;

	public ProductManagerBackedCommonAssetAttributeFinder(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds) {
		return assetManager.findAllCommonInfoFieldNames(assetTypeIds);
	}
	

}