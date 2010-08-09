package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;


public class AssetIdListResponse extends RequestResponse {
	private List<Long> assetIds = new ArrayList<Long>();

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public void setAssetIds(List<Long> productIds) {
		this.assetIds = productIds;
	}
}
