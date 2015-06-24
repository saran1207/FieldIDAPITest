package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2015-06-23.
 */
public class SmartSearchSelectCell extends Panel {

    public SmartSearchSelectCell(String id, IModel<? extends Asset> assetModel) {
        super(id);

        add(new BookmarkablePageLink<Void>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetModel.getObject().getId()))
                        .add(new Label("label", "Select")));
    }
}

