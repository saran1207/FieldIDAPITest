package com.n4systems.model.utils;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;

public interface AssetEvent {

    public Asset getAsset();
    public void setAsset(Asset asset);

    public AssetStatus getAssetStatus();
    public void setAssetStatus(AssetStatus status);

}
