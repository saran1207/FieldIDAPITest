package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.services.search.EventFullTextSearchService;
import com.n4systems.services.search.SearchResults;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventTextSearchDataProvider extends TextSearchDataProvider {

    private @SpringBean EventFullTextSearchService fullTextSearchService;

    public EventTextSearchDataProvider(IModel<String> searchText) {
        super(searchText);
    }

    @Override
    protected SearchResults performSearch(String searchText, Formatter formatter, int first, int count) {
        return fullTextSearchService.search(searchText, formatter, first, count);
    }

    @Override
    protected SearchResults performSearch(String searchText, Formatter formatter) {
        return fullTextSearchService.search(searchText, formatter);
    }

    @Override
    protected SearchResults count(String searchText) {
        return fullTextSearchService.count(searchText);
    }

}
