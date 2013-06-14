package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.services.brainforest.SearchQuery;
import org.apache.lucene.document.Document;

import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {

    private List<SearchResult> results = Lists.newArrayList();
    private SearchQuery searchQuery;


    public SearchResults() {
        // TODO DD : get query string in here...
    }

    public SearchResults add(Document doc) {
        results.add(new SearchResult(doc));
        return this;
    }

    public List<SearchResult> getResults() {
        return results;
    }
}
