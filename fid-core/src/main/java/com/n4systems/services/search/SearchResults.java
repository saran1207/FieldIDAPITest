package com.n4systems.services.search;

import com.google.common.collect.Lists;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {

    private List<SearchResult> results = Lists.newArrayList();

    public SearchResults add(Document doc) {
        results.add(new SearchResult(doc, getHighlighter(), getAnalyzer()));
        return this;
    }

    public SearchResults add(IndexSearcher searcher, ScoreDoc... docs) {
        try {
            for (ScoreDoc scoreDoc:docs) {
                add(searcher.doc(scoreDoc.doc));
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

}
