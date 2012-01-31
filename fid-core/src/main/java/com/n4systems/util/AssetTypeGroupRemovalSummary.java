package com.n4systems.util;

import com.n4systems.model.AssetTypeGroup;

public class AssetTypeGroupRemovalSummary {
	private AssetTypeGroup group;

	private Long assetTypesConnected = 0L;
    private Long savedReportsConnected = 0L;

	public AssetTypeGroupRemovalSummary(AssetTypeGroup group) {
		this.group = group;
	}

	public AssetTypeGroup getGroup() {
		return group;
	}

	public Long getAssetTypesConnected() {
		return assetTypesConnected;
	}

	public void setAssetTypesConnected(Long assetTypesConnected) {
		this.assetTypesConnected = assetTypesConnected;
	}

    public Long getSavedReportsConnected() {
        return savedReportsConnected;
    }

    public void setSavedReportsConnected(Long savedReportsConnected) {
        this.savedReportsConnected = savedReportsConnected;
    }
}
