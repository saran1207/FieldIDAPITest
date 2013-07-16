package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.SearchResults;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetTextSearchDataProvider extends TextSearchDataProvider {

    private @SpringBean AssetFullTextSearchService fullTextSearchService;

    public AssetTextSearchDataProvider(IModel<String> searchText) {
        super(searchText);
    }

    @Override
    protected SearchResults performSearch(String searchText, Formatter formatter, int first, int count) {
        return fullTextSearchService.search(searchText, getFormatter(), first, count);
    }

    @Override
    protected SearchResults performSearch(String searchText, Formatter formatter) {
        return  fullTextSearchService.search(searchText, getFormatter());
    }

    @Override
    protected SearchResults count(String searchText) {
        return fullTextSearchService.count(searchText);
    }

}
