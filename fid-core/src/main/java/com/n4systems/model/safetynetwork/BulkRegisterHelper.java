package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;

import java.util.ArrayList;
import java.util.List;

public class BulkRegisterHelper {

    private AllPreAssignedAssetsLoader preAssignedAssetLoader;

    public BulkRegisterHelper(AllPreAssignedAssetsLoader preAssignedAssetLoader) {
        this.preAssignedAssetLoader = preAssignedAssetLoader;
    }

    public BulkRegisterData calculateBulkRegisterAssetTypeCounts() {
        BulkRegisterData data = new BulkRegisterData();

        List<Asset> assets = preAssignedAssetLoader.load();

        for (Asset asset : assets) {
            AssetType type = asset.getType();
            data.addItemOfType(type);
        }

        return data;
    }

    public List<Asset> getPreassignedAssetsOfType(Long assetTypeId) {
        List<Asset> preAssignedAssetsOfCorrectType = new ArrayList<Asset>();

        List<Asset> preAssignedAssets = preAssignedAssetLoader.load();
        for (Asset preAssignedAsset : preAssignedAssets) {
            if (preAssignedAsset.getType().getId().equals(assetTypeId)) {
                preAssignedAssetsOfCorrectType.add(preAssignedAsset);
            }
        }

        return preAssignedAssetsOfCorrectType;
    }

}

