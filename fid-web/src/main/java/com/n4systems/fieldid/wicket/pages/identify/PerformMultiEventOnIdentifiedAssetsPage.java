package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import java.util.List;

public class PerformMultiEventOnIdentifiedAssetsPage extends FieldIDFrontEndPage {

    public PerformMultiEventOnIdentifiedAssetsPage(List<Long> assetIds) {

        add(new ListView<Long>("assetIdList", assetIds) {
            @Override
            protected void populateItem(ListItem<Long> item) {
                WebMarkupContainer hiddenInputWithAssetId= new WebMarkupContainer("assetId");
                hiddenInputWithAssetId.add(new AttributeModifier("name", "assetIds["+item.getIndex()+"]"));
                hiddenInputWithAssetId.add(new AttributeModifier("value", item.getModelObject()));
            }
        });

    }

}
