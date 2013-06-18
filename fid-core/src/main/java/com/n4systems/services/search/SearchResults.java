package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.services.brainforest.SearchQuery;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {

    private List<SearchResult> results = Lists.newArrayList();
    private SearchQuery searchQuery;
    private @Transient NumericHighlighter highlighter;
    private @Transient Analyzer analyzer;
    private @Transient Formatter formatter;
    private @Transient NumericAnalyzer numericAnalyzer = new NumericAnalyzer();

    private String preTag = "</span>";
    private String postTag  = "<span class=\"matched-text\">";


    public SearchResults() {
        // TODO DD : get query string in here...
    }

    public SearchResults(Formatter formatter, Query query, Analyzer analyzer) {
        super();
        withHighlighting(query, analyzer, formatter);
    }

    public SearchResults withHighlighting(Query query, Analyzer analyzer, Formatter formatter) {
        QueryScorer queryScorer = new QueryScorer(query);
        this.formatter = formatter;
        highlighter = new NumericHighlighter(formatter, queryScorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, Integer.MAX_VALUE));
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
        this.analyzer = analyzer;
        return this;
    }

    public SearchResults add(Document doc) {
        results.add(new SearchResult(doc, highlighter, analyzer));
        return this;
    }

    public List<SearchResult> getResults() {
        return results;
    }

}
