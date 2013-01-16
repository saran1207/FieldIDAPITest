package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.platform.PlatformInformationIcon;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

public class AssetDetailsPanel extends Panel {

    public AssetDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        String created = new FieldIdDateFormatter(asset.getCreated(), FieldIDSession.get().getSessionUser(), true, true).format();
        String modified = new FieldIdDateFormatter(asset.getModified(), FieldIDSession.get().getSessionUser(), true, true).format();
        String identified = new FieldIdDateFormatter(asset.getIdentified(), FieldIDSession.get().getSessionUser(), false, false).format();

        add(new Label("referenceNumber", ProxyModel.of(asset, on(Asset.class).getCustomerRefNumber())));
        add(new Label("rfidNumber", ProxyModel.of(asset, on(Asset.class).getRfidNumber())));

        WebMarkupContainer createdContainer;
        add(createdContainer = new WebMarkupContainer("createdContainer"));
        createdContainer.add(new Label("created", created));
        createdContainer.add(new Label("createdby", ProxyModel.of(asset, on(Asset.class).getCreatedBy().getFullName())));

        PlatformInformationIcon createdIcon;
        add(createdIcon = new PlatformInformationIcon("createdPlatform",
                ProxyModel.of(asset, on(Asset.class).getCreatedPlatformType()),
                ProxyModel.of(asset, on(Asset.class).getCreatedPlatform())));

        if (createdIcon.isVisible()) {
            createdContainer.add(new AttributeAppender("class", "withIcon").setSeparator(" "));
        }else {
            createdContainer.add(new AttributeAppender("class", "withoutIcon").setSeparator(" "));
        }

        WebMarkupContainer modifiedContainer;
        add(modifiedContainer = new WebMarkupContainer("modifiedContainer"));
        modifiedContainer.add(new Label("modified", modified));
        modifiedContainer.add(new Label("modifiedBy", ProxyModel.of(asset, on(Asset.class).getModifiedBy().getFullName())));

        PlatformInformationIcon modifiedIcon;
        add(modifiedIcon = new PlatformInformationIcon("modifiedPlatform",
                ProxyModel.of(asset, on(Asset.class).getModifiedPlatformType()),
                ProxyModel.of(asset, on(Asset.class).getModifiedPlatform())));

        if(modifiedIcon.isVisible()) {
            modifiedContainer.add(new AttributeAppender("class", "withIcon").setSeparator(" "));
        }else {
            modifiedContainer.add(new AttributeAppender("class", "withoutIcon").setSeparator(" "));
        }

        add(new Label("visibility", ProxyModel.of(asset, on(Asset.class).isPublished())));
        add(new Label("identified", identified));
    }

}
