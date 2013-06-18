package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.services.brainforest.SearchQuery;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {

    private List<SearchResult> results = Lists.newArrayList();
    private SearchQuery searchQuery;


    public SearchResults() {
        super();
    }

    public SearchResults add(Document doc) {
        results.add(new SearchResult(doc, getHighlighter(), getAnalyzer()));
        return this;
    }

    protected Analyzer getAnalyzer() {
        return null;// override if you want highlighting to occur.
    }

    protected NumericHighlighter getHighlighter() {
        return null;  // override if you want highlighting to occur.
    }

    public List<SearchResult> getResults() {
        return results;
    }

}
