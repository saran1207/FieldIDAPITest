package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Joiner;
import com.n4systems.model.Asset;
import org.apache.wicket.model.Model;

public class AssetLabelModel extends Model<String> {

    private Asset asset;

    public AssetLabelModel(Asset asset) {
        this.asset = asset;
    }

    @Override
    public String getObject() {
        Asset asset = this.asset;
        Joiner joiner = Joiner.on(" / ").skipNulls();
        String result = joiner.join(asset.getType().getName(), asset.getIdentifier(), asset.getAssetStatus());
        return result;
    }

}
