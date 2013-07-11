package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.services.search.parser.SearchQuery;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchResults implements Serializable {

    private List<SearchResult> results = Lists.newArrayList();
    private SearchQuery searchQuery = null;

    @Deprecated  // use constructor with searchQuery param.
    SearchResults() {
    }

    public SearchResults(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchResults add(Document doc) {
        results.add(new SearchResult(doc, getHighlighter(), getAnalyzer()));
        return this;
    }

    public SearchResults add(IndexSearcher searcher, ScoreDoc... docs) {
        try {
            for (ScoreDoc scoreDoc:docs) {
                if (scoreDoc!=null) {
                    add(searcher.doc(scoreDoc.doc));
                }
            }
        } catch (IOException e) {
            ;
        }
        return this;
    }

    protected Analyzer getAnalyzer() {
        return null;// override if you want highlighting to occur.
    }

    protected FieldIdHighlighter getHighlighter() {
        return null;  // override if you want highlighting to occur.
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public int getCount() {
        return results.size();
    }

    public Set<String> getSuggestions() {
        return searchQuery==null ? new HashSet<String>() : searchQuery.getSuggestions();
    }
}
