package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ProcedureTitleLabel extends Panel {

    public ProcedureTitleLabel(String id, IModel<Asset> model) {
        super(id, model);
        Asset asset = model.getObject();
        BookmarkablePageLink assetLink;
        String assetLabel = asset.getType().getDisplayName() + " / " + asset.getIdentifier();

        add(assetLink = new BookmarkablePageLink<AssetSummaryPage>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId())));
        assetLink.add(new Label("label", assetLabel).setRenderBodyOnly(true));
    }
}
