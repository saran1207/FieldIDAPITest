package com.n4systems.fieldid.wicket.pages.search;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.SearchResults;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.*;


public abstract class TextSearchDataProvider extends FieldIDDataProvider<SearchResult> {

    private IModel<String> searchText;
    // caching.
    private transient Integer size = null;
    private transient List<SearchResult> pageResults = null;
    private transient Set<String> suggestions = null;

    public TextSearchDataProvider(IModel<String> searchText) {
        this.searchText = searchText;
    }

    public List<String> getSuggestions() {
        return suggestions==null ? new ArrayList<String>() : Lists.newArrayList(suggestions);
    }

    @Override
    public Iterator<? extends SearchResult> iterator(int first, int count) {
        if (StringUtils.isBlank(searchText.getObject())) {
            return Collections.<SearchResult>emptyList().iterator();
        }
        if (pageResults==null) {
            SearchResults searchResults = performSearch(searchText.getObject(), getFormatter(), first, count);//fullTextSearchService.search(searchText.getObject(), getFormatter(), first, count);
            suggestions = searchResults.getSuggestions();
            pageResults = searchResults.getResults();
        }
        return pageResults.iterator();
    }

    protected Formatter getFormatter() {
        return null;
    }

    @Override
    public int size() {
        if (StringUtils.isBlank(searchText.getObject())) {
            return 0;
        }
        if (size==null) {
            SearchResults searchResults = count(searchText.getObject());
            size = searchResults.getCount();
            suggestions = searchResults.getSuggestions();
        }
        return size;
    }

    public List<Long> getIdList() {
        SearchResults searchResults = performSearch(searchText.getObject(), getFormatter());
        List<SearchResult> results = searchResults.getResults();

        List<Long> ids = new ArrayList<Long>();

        for (SearchResult searchResult: results) {
            ids.add(searchResult.getLong(AssetIndexField.ID.getField()));
        }

        return ids;
    }

    protected abstract SearchResults performSearch(String searchText, Formatter formatter, int first, int count);
    protected abstract SearchResults performSearch(String searchText, Formatter formatter);
    protected abstract SearchResults count(String searchText);


    @Override
    public IModel<SearchResult> model(SearchResult object) {
        return new Model<SearchResult>(object);
    }

    @Override
    public void detach() {
        searchText.detach();
        pageResults = null;
        size = null;
        suggestions = null;
    }
}
