package com.n4systems.fieldid.wicket.pages.useraccount.table;


import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssetProfileLinkCell extends Panel {

    public AssetProfileLinkCell(String id, IModel<Asset> model) {
        super(id, model);
        Link link;
        add(link = new BookmarkablePageLink("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(model.getObject().getId())));
        link.add(new Label("identifier", new PropertyModel(model, "identifier")));
    }
}
