package com.n4systems.fieldid.wicket.components.assettypegroup;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.EditAssetTypeGroupPage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by tracyshi on 2014-08-01.
 */
public class AssetTypeGroupDetailsPanel extends Panel {

    public AssetTypeGroupDetailsPanel(String id, AssetTypeGroup assetTypeGroup) {
        super(id);
        add(new BookmarkablePageLink<Void>("editLink", EditAssetTypeGroupPage.class, PageParametersBuilder.uniqueId(assetTypeGroup.getId())));
        add(new Label("name", new PropertyModel<String>(assetTypeGroup, "displayName")));
    }

}
