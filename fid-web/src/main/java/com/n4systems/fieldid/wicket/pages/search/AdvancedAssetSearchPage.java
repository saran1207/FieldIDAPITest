package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.wicket.pages.search.details.AssetDetailsPanel;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

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
}
