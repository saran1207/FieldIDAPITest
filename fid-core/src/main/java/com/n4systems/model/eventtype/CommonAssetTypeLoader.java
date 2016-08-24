package com.n4systems.model.eventtype;

import com.n4systems.model.AssetType;
import com.n4systems.persistence.Transaction;

import java.util.List;

public interface CommonAssetTypeLoader {

	public CommonAssetTypeLoader forAssets(List<Long> assetIds);

	public List<AssetType> load();
	
	public List<AssetType> load(Transaction transaction);
	

}