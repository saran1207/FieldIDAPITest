package com.n4systems.webservice.assetdownload;

import com.n4systems.webservice.dto.RequestResponse;

import java.util.ArrayList;
import java.util.List;


public class AssetIdListResponse extends RequestResponse {
	private List<SyncAsset> assets = new ArrayList<SyncAsset>();

	public List<SyncAsset> getAssets() {
		return assets;
	}

	public void setAssets(List<SyncAsset> assets) {
		this.assets = assets;
	}
}
