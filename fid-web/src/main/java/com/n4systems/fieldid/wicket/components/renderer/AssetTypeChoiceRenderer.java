package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class AssetTypeChoiceRenderer implements IChoiceRenderer<AssetType> {

    @Override
    public Object getDisplayValue(AssetType assetType) {
        return assetType.getName();
    }

    @Override
    public String getIdValue(AssetType assetType, int index) {
        return assetType.getId()+"";
    }

}
