package com.n4systems.fieldid.wicket.components.feedback.classy;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.MapModel;

import java.util.HashMap;
import java.util.Map;

public class AssetCreatedFeedbackMessageDisplayPanel extends Panel {

    public AssetCreatedFeedbackMessageDisplayPanel(String id, AssetCreatedFeedbackMessage message) {
        super(id);

        Map<String,String>  valueMap = new HashMap<String, String>();
        valueMap.put("identifier", message.getIdentifier());
        add(new FlatLabel("createdLabel", new StringResourceModel("message.asset_created", this, new MapModel<String,String>(valueMap))));
        add(new BookmarkablePageLink<Void>("link", AssetSummaryPage.class, PageParametersBuilder.uniqueId(message.getNewAssetId())));
    }
}
