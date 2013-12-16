package com.n4systems.fieldid.wicket.pages.event.target;

import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import static ch.lambdaj.Lambda.on;

public class AssetDetailsPanel extends Panel {

    public AssetDetailsPanel(String id, IModel<Asset> model) {
        super(id);

        add(new Label("assetTypeName", ProxyModel.of(model, on(Asset.class).getType().getName())));
        add(new Label("description", ProxyModel.of(model, on(Asset.class).getDescription())));

        add(new IdentifierLabel("identifierLabel", ProxyModel.of(model, on(Asset.class).getType())));

        add(new Label("rfidNumber", ProxyModel.of(model, on(Asset.class).getRfidNumber())));
        add(new Label("referenceNumber", ProxyModel.of(model, on(Asset.class).getCustomerRefNumber())));

        add(createSummaryLink(model));
    }

    protected Component createSummaryLink(IModel<Asset> model) {
        BookmarkablePageLink<Void> assetLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(model.getObject().getId()));
        assetLink.add(new Label("identifier", new PropertyModel<Object>(model, "identifier")));
        return assetLink;
    }

}
