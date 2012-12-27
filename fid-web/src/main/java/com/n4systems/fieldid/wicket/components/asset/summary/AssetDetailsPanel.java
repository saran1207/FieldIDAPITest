package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Asset;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssetDetailsPanel extends Panel {
    public AssetDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        String created = new FieldIdDateFormatter(asset.getCreated(), FieldIDSession.get().getSessionUser(), false, true).format();
        String modified = new FieldIdDateFormatter(asset.getModified(), FieldIDSession.get().getSessionUser(), false, true).format();
        String identified = new FieldIdDateFormatter(asset.getIdentified(), FieldIDSession.get().getSessionUser(), false, false).format();

        add(new Label("referenceNumber", new PropertyModel<Object>(asset, "customerRefNumber")));
        add(new Label("rfidNumber", new PropertyModel<Object>(asset, "rfidNumber")));
        add(new Label("createdby", new PropertyModel<Object>(asset, "createdBy.fullName")));
        add(new Label("created", created));
        add(new Label("modifiedBy", new PropertyModel<Object>(asset, "modifiedBy.fullName")));
        add(new Label("modified", modified));
        add(new Label("visibility", new PropertyModel<Object>(asset, "published")));
        add(new Label("identified", identified));
    }
}
