package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssetDetailsPanel extends Panel {
    public AssetDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        add(new Label("referenceNumber", new PropertyModel<Object>(asset, "customerRefNumber")));
        add(new Label("rfidNumber", new PropertyModel<Object>(asset, "rfidNumber")));
        add(new Label("identifiedBy", new PropertyModel<Object>(asset, "identifiedBy.fullName")));
        add(new Label("modifiedBy", new PropertyModel<Object>(asset, "modifiedBy.fullName")));
        add(new Label("visibility", new PropertyModel<Object>(asset, "published")));
        add(new Label("identified", new PropertyModel<Object>(asset, "identified")));
    }
}
