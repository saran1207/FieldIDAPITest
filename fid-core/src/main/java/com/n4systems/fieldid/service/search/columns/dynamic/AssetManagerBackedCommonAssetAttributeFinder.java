package com.n4systems.fieldid.service.search.columns.dynamic;

import java.util.List;
import java.util.SortedSet;

import com.n4systems.ejb.AssetManager;

public class AssetManagerBackedCommonAssetAttributeFinder implements CommonAssetAttributeFinder {
	private final AssetManager assetManager;

	public AssetManagerBackedCommonAssetAttributeFinder(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds) {
		return assetManager.findAllCommonInfoFieldNames(assetTypeIds);
	}
	

}