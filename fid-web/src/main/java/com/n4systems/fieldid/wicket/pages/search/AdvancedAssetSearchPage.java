package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.search.actions.AssetTextSearchActionsPanel;
import com.n4systems.fieldid.wicket.pages.search.details.AssetDetailsPanel;
import com.n4systems.fieldid.wicket.pages.search.help.AssetSearchHelpPanel;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.util.Set;

public class AdvancedAssetSearchPage extends AdvancedSearchPage {

    public AdvancedAssetSearchPage() {
        super(AssetIndexField.ID);
    }

    @Override
    protected TextSearchDataProvider createDataProvider(IModel<String> searchTextModel) {
        return  new AssetTextSearchDataProvider(searchTextModel) {
            @Override protected Formatter getFormatter() {
                return new SimpleHTMLFormatter("<span class=\"matched-text\">", "</span>");
            }
        };
    }

    @Override
    protected Component createDetailsPanel(String id, IModel<SearchResult> resultModel) {
        return new AssetDetailsPanel(id, resultModel);
    }

    @Override
    protected Component createActionsPanel(String id, IModel<Set<Long>> selectedItemsModel) {
        return new AssetTextSearchActionsPanel(id, selectedItemsModel);
    }

    @Override
    protected Component createHelpPanel(String id) {
        return new AssetSearchHelpPanel(id);
    }

    @Override
    protected Component createSelectItemsLabel(String id) {
        return new Label(id, new FIDLabelModel("label.select_assets"));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.assetsearch"));
    }
}
