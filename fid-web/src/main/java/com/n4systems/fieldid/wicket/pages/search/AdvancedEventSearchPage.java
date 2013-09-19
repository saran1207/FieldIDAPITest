package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.search.actions.EventTextSearchActionsPanel;
import com.n4systems.fieldid.wicket.pages.search.details.EventDetailsPanel;
import com.n4systems.fieldid.wicket.pages.search.help.EventSearchHelpPanel;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.EventIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
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

    @Override
    protected Component createHelpPanel(String id) {
        return new EventSearchHelpPanel(id);
    }

    @Override
    protected Component createSelectItemsLabel(String id) {
        return new Label(id, new FIDLabelModel("label.select_events"));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.advanced_event_search"));
    }
}
