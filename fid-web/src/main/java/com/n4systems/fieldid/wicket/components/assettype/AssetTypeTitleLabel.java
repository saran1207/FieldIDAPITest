package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssetTypeTitleLabel extends Panel {
    public AssetTypeTitleLabel(String labelId, IModel<AssetType> assetType) {
        super(labelId, assetType);

        if(assetType.getObject().isNew()) {
            add(new Label("label", new FIDLabelModel("label.new")));
        } else {
            add(new Label("label", new PropertyModel<String>(assetType, "name")));
        }
    }
}
