package com.n4systems.model.inspectiontype;

import java.util.List;

import com.n4systems.model.AssetType;
import com.n4systems.persistence.Transaction;

public interface CommonAssetTypeLoader {

	public CommonAssetTypeLoader forAssets(List<Long> assetIds);

	public List<AssetType> load();
	
	public List<AssetType> load(Transaction transaction);
	

}