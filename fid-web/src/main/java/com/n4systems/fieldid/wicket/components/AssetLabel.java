package com.n4systems.fieldid.wicket.components;

import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AssetLabel extends Panel {
    public AssetLabel(String id, IModel<Asset> model) {
        super(id, model);

        Asset asset = model.getObject();
        add(new Label("assetType", asset.getType().getName()));
        add(new Label("assetIdentifier", asset.getIdentifier()));
        Label assetStatus;
        if(asset.getAssetStatus() != null) {
            add(assetStatus = new Label("assetStatus", asset.getAssetStatus().getDisplayName()));
        } else {
            add(assetStatus = new Label("assetStatus"));
            assetStatus.setVisible(false);
        }
    }
}
