package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.wicket.pages.search.actions.EventTextSearchActionsPanel;
import com.n4systems.fieldid.wicket.pages.search.details.EventDetailsPanel;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.EventIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import java.util.Set;

public class AdvancedEventSearchPage extends AdvancedSearchPage {

    public AdvancedEventSearchPage() {
        super(EventIndexField.ID);
    }

    @Override
    protected TextSearchDataProvider createDataProvider(IModel<String> searchTextModel) {
        return new EventTextSearchDataProvider(searchTextModel) {
            @Override protected Formatter getFormatter() {
                return new SimpleHTMLFormatter("<span class=\"matched-text\">", "</span>");
            }
        };
    }

    @Override
    protected Component createDetailsPanel(String id, IModel<SearchResult> resultModel) {
        return new EventDetailsPanel(id, resultModel);
    }

    @Override
    protected Component createActionsPanel(String id, IModel<Set<String>> selectedItemsModel) {
        return new EventTextSearchActionsPanel(id, selectedItemsModel);
    }
}
