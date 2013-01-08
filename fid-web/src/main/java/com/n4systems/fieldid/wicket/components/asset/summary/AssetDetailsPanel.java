package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.platform.PlatformInformationIcon;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

public class AssetDetailsPanel extends Panel {

    public AssetDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        String created = new FieldIdDateFormatter(asset.getCreated(), FieldIDSession.get().getSessionUser(), false, true).format();
        String modified = new FieldIdDateFormatter(asset.getModified(), FieldIDSession.get().getSessionUser(), false, true).format();
        String identified = new FieldIdDateFormatter(asset.getIdentified(), FieldIDSession.get().getSessionUser(), false, false).format();

        add(new Label("referenceNumber", ProxyModel.of(asset, on(Asset.class).getCustomerRefNumber())));
        add(new Label("rfidNumber", ProxyModel.of(asset, on(Asset.class).getRfidNumber())));

        add(new Label("created", created));
        add(new Label("createdby", ProxyModel.of(asset, on(Asset.class).getCreatedBy().getFullName())));

        add(new PlatformInformationIcon("createdPlatform",
                ProxyModel.of(asset, on(Asset.class).getCreatedPlatformType()),
                ProxyModel.of(asset, on(Asset.class).getCreatedPlatform())));

        add(new Label("modified", modified));
        add(new Label("modifiedBy", ProxyModel.of(asset, on(Asset.class).getModifiedBy().getFullName())));

        add(new PlatformInformationIcon("modifiedPlatform",
                ProxyModel.of(asset, on(Asset.class).getModifiedPlatformType()),
                ProxyModel.of(asset, on(Asset.class).getModifiedPlatform())));

        add(new Label("visibility", ProxyModel.of(asset, on(Asset.class).isPublished())));
        add(new Label("identified", identified));
    }

}
