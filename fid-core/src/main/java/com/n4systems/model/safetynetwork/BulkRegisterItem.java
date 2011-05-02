package com.n4systems.model.safetynetwork;

import com.n4systems.model.AssetType;

public class BulkRegisterItem {

    private AssetType assetType;
    private Long count = 0L;

    public BulkRegisterItem(AssetType assetType) {
        this.assetType = assetType;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void incrementCount() {
        count += 1;
    }
}
