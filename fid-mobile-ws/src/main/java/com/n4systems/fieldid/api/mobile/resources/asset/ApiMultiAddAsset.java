package com.n4systems.fieldid.api.mobile.resources.asset;

import java.util.List;

public class ApiMultiAddAsset {
	private List<ApiAssetIdentifiers> identifiers;
	private ApiAsset assetTemplate;

	public List<ApiAssetIdentifiers> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<ApiAssetIdentifiers> identifiers) {
		this.identifiers = identifiers;
	}

	public ApiAsset getAssetTemplate() {
		return assetTemplate;
	}

	public void setAssetTemplate(ApiAsset assetTemplate) {
		this.assetTemplate = assetTemplate;
	}

}
